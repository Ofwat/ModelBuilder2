(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('validation-rule', {
            parent: 'entity',
            url: '/validation-rule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRules'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule/validation-rules.html',
                    controller: 'ValidationRuleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('validation-rule-detail', {
            parent: 'entity',
            url: '/validation-rule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRule'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule/validation-rule-detail.html',
                    controller: 'ValidationRuleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ValidationRule', function($stateParams, ValidationRule) {
                    return ValidationRule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'validation-rule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('validation-rule-detail.edit', {
            parent: 'validation-rule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule/validation-rule-dialog.html',
                    controller: 'ValidationRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRule', function(ValidationRule) {
                            return ValidationRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule.new', {
            parent: 'validation-rule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule/validation-rule-dialog.html',
                    controller: 'ValidationRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('validation-rule', null, { reload: 'validation-rule' });
                }, function() {
                    $state.go('validation-rule');
                });
            }]
        })
        .state('validation-rule.edit', {
            parent: 'validation-rule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule/validation-rule-dialog.html',
                    controller: 'ValidationRuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRule', function(ValidationRule) {
                            return ValidationRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule', null, { reload: 'validation-rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule.delete', {
            parent: 'validation-rule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule/validation-rule-delete-dialog.html',
                    controller: 'ValidationRuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ValidationRule', function(ValidationRule) {
                            return ValidationRule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule', null, { reload: 'validation-rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
