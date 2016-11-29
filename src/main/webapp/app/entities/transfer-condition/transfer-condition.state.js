(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer-condition', {
            parent: 'entity',
            url: '/transfer-condition',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferConditions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-condition/transfer-conditions.html',
                    controller: 'TransferConditionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transfer-condition-detail', {
            parent: 'entity',
            url: '/transfer-condition/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferCondition'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-condition/transfer-condition-detail.html',
                    controller: 'TransferConditionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransferCondition', function($stateParams, TransferCondition) {
                    return TransferCondition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer-condition',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-condition-detail.edit', {
            parent: 'transfer-condition-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-condition/transfer-condition-dialog.html',
                    controller: 'TransferConditionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferCondition', function(TransferCondition) {
                            return TransferCondition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-condition.new', {
            parent: 'transfer-condition',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-condition/transfer-condition-dialog.html',
                    controller: 'TransferConditionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemCode: null,
                                yearCode: null,
                                value: null,
                                failureMessage: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfer-condition', null, { reload: 'transfer-condition' });
                }, function() {
                    $state.go('transfer-condition');
                });
            }]
        })
        .state('transfer-condition.edit', {
            parent: 'transfer-condition',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-condition/transfer-condition-dialog.html',
                    controller: 'TransferConditionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferCondition', function(TransferCondition) {
                            return TransferCondition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-condition', null, { reload: 'transfer-condition' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-condition.delete', {
            parent: 'transfer-condition',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-condition/transfer-condition-delete-dialog.html',
                    controller: 'TransferConditionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransferCondition', function(TransferCondition) {
                            return TransferCondition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-condition', null, { reload: 'transfer-condition' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
