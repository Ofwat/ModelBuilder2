(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('validation-rule-details', {
            parent: 'entity',
            url: '/validation-rule-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRuleDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details.html',
                    controller: 'ValidationRuleDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('validation-rule-details-detail', {
            parent: 'entity',
            url: '/validation-rule-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRuleDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details-detail.html',
                    controller: 'ValidationRuleDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ValidationRuleDetails', function($stateParams, ValidationRuleDetails) {
                    return ValidationRuleDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'validation-rule-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('validation-rule-details-detail.edit', {
            parent: 'validation-rule-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details-dialog.html',
                    controller: 'ValidationRuleDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRuleDetails', function(ValidationRuleDetails) {
                            return ValidationRuleDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule-details.new', {
            parent: 'validation-rule-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details-dialog.html',
                    controller: 'ValidationRuleDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                formula: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('validation-rule-details', null, { reload: 'validation-rule-details' });
                }, function() {
                    $state.go('validation-rule-details');
                });
            }]
        })
        .state('validation-rule-details.edit', {
            parent: 'validation-rule-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details-dialog.html',
                    controller: 'ValidationRuleDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRuleDetails', function(ValidationRuleDetails) {
                            return ValidationRuleDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule-details', null, { reload: 'validation-rule-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule-details.delete', {
            parent: 'validation-rule-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-details/validation-rule-details-delete-dialog.html',
                    controller: 'ValidationRuleDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ValidationRuleDetails', function(ValidationRuleDetails) {
                            return ValidationRuleDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule-details', null, { reload: 'validation-rule-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
