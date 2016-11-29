(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('validation-rule-item', {
            parent: 'entity',
            url: '/validation-rule-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRuleItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-items.html',
                    controller: 'ValidationRuleItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('validation-rule-item-detail', {
            parent: 'entity',
            url: '/validation-rule-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ValidationRuleItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-item-detail.html',
                    controller: 'ValidationRuleItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ValidationRuleItem', function($stateParams, ValidationRuleItem) {
                    return ValidationRuleItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'validation-rule-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('validation-rule-item-detail.edit', {
            parent: 'validation-rule-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-item-dialog.html',
                    controller: 'ValidationRuleItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRuleItem', function(ValidationRuleItem) {
                            return ValidationRuleItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule-item.new', {
            parent: 'validation-rule-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-item-dialog.html',
                    controller: 'ValidationRuleItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                evaluate: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('validation-rule-item', null, { reload: 'validation-rule-item' });
                }, function() {
                    $state.go('validation-rule-item');
                });
            }]
        })
        .state('validation-rule-item.edit', {
            parent: 'validation-rule-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-item-dialog.html',
                    controller: 'ValidationRuleItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ValidationRuleItem', function(ValidationRuleItem) {
                            return ValidationRuleItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule-item', null, { reload: 'validation-rule-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('validation-rule-item.delete', {
            parent: 'validation-rule-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/validation-rule-item/validation-rule-item-delete-dialog.html',
                    controller: 'ValidationRuleItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ValidationRuleItem', function(ValidationRuleItem) {
                            return ValidationRuleItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('validation-rule-item', null, { reload: 'validation-rule-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
