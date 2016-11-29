(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro', {
            parent: 'entity',
            url: '/macro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Macros'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro/macros.html',
                    controller: 'MacroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('macro-detail', {
            parent: 'entity',
            url: '/macro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Macro'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro/macro-detail.html',
                    controller: 'MacroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Macro', function($stateParams, Macro) {
                    return Macro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'macro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('macro-detail.edit', {
            parent: 'macro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-dialog.html',
                    controller: 'MacroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Macro', function(Macro) {
                            return Macro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro.new', {
            parent: 'macro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-dialog.html',
                    controller: 'MacroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                block: null,
                                conditionalItemCode: null,
                                pageCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: 'macro' });
                }, function() {
                    $state.go('macro');
                });
            }]
        })
        .state('macro.edit', {
            parent: 'macro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-dialog.html',
                    controller: 'MacroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Macro', function(Macro) {
                            return Macro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: 'macro' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro.delete', {
            parent: 'macro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-delete-dialog.html',
                    controller: 'MacroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Macro', function(Macro) {
                            return Macro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: 'macro' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
