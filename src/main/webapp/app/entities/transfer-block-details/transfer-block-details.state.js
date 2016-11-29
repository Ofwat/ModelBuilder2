(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer-block-details', {
            parent: 'entity',
            url: '/transfer-block-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlockDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details.html',
                    controller: 'TransferBlockDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transfer-block-details-detail', {
            parent: 'entity',
            url: '/transfer-block-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlockDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details-detail.html',
                    controller: 'TransferBlockDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransferBlockDetails', function($stateParams, TransferBlockDetails) {
                    return TransferBlockDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer-block-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-block-details-detail.edit', {
            parent: 'transfer-block-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details-dialog.html',
                    controller: 'TransferBlockDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlockDetails', function(TransferBlockDetails) {
                            return TransferBlockDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block-details.new', {
            parent: 'transfer-block-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details-dialog.html',
                    controller: 'TransferBlockDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fromModelCode: null,
                                fromVersionCode: null,
                                fromPageCode: null,
                                toModelCode: null,
                                toVersionCode: null,
                                toPageCode: null,
                                toMacroCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfer-block-details', null, { reload: 'transfer-block-details' });
                }, function() {
                    $state.go('transfer-block-details');
                });
            }]
        })
        .state('transfer-block-details.edit', {
            parent: 'transfer-block-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details-dialog.html',
                    controller: 'TransferBlockDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlockDetails', function(TransferBlockDetails) {
                            return TransferBlockDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block-details', null, { reload: 'transfer-block-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block-details.delete', {
            parent: 'transfer-block-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-details/transfer-block-details-delete-dialog.html',
                    controller: 'TransferBlockDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransferBlockDetails', function(TransferBlockDetails) {
                            return TransferBlockDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block-details', null, { reload: 'transfer-block-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
