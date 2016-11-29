(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('page', {
            parent: 'entity',
            url: '/page',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page/pages.html',
                    controller: 'PageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('page-detail', {
            parent: 'entity',
            url: '/page/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Page'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page/page-detail.html',
                    controller: 'PageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Page', function($stateParams, Page) {
                    return Page.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'page',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('page-detail.edit', {
            parent: 'page-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-dialog.html',
                    controller: 'PageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Page', function(Page) {
                            return Page.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page.new', {
            parent: 'page',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-dialog.html',
                    controller: 'PageDialogController',
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
                    $state.go('page', null, { reload: 'page' });
                }, function() {
                    $state.go('page');
                });
            }]
        })
        .state('page.edit', {
            parent: 'page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-dialog.html',
                    controller: 'PageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Page', function(Page) {
                            return Page.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page', null, { reload: 'page' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page.delete', {
            parent: 'page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-delete-dialog.html',
                    controller: 'PageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Page', function(Page) {
                            return Page.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page', null, { reload: 'page' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
