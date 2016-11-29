(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-page', {
            parent: 'entity',
            url: '/company-page',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CompanyPages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-page/company-pages.html',
                    controller: 'CompanyPageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('company-page-detail', {
            parent: 'entity',
            url: '/company-page/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CompanyPage'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-page/company-page-detail.html',
                    controller: 'CompanyPageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CompanyPage', function($stateParams, CompanyPage) {
                    return CompanyPage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'company-page',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('company-page-detail.edit', {
            parent: 'company-page-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-page/company-page-dialog.html',
                    controller: 'CompanyPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyPage', function(CompanyPage) {
                            return CompanyPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-page.new', {
            parent: 'company-page',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-page/company-page-dialog.html',
                    controller: 'CompanyPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyCode: null,
                                pageCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-page', null, { reload: 'company-page' });
                }, function() {
                    $state.go('company-page');
                });
            }]
        })
        .state('company-page.edit', {
            parent: 'company-page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-page/company-page-dialog.html',
                    controller: 'CompanyPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyPage', function(CompanyPage) {
                            return CompanyPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-page', null, { reload: 'company-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-page.delete', {
            parent: 'company-page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-page/company-page-delete-dialog.html',
                    controller: 'CompanyPageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyPage', function(CompanyPage) {
                            return CompanyPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-page', null, { reload: 'company-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
