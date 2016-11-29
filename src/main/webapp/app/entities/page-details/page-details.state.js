(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('page-details', {
            parent: 'entity',
            url: '/page-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PageDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page-details/page-details.html',
                    controller: 'PageDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('page-details-detail', {
            parent: 'entity',
            url: '/page-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PageDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page-details/page-details-detail.html',
                    controller: 'PageDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PageDetails', function($stateParams, PageDetails) {
                    return PageDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'page-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('page-details-detail.edit', {
            parent: 'page-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-details/page-details-dialog.html',
                    controller: 'PageDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PageDetails', function(PageDetails) {
                            return PageDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page-details.new', {
            parent: 'page-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-details/page-details-dialog.html',
                    controller: 'PageDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                text: null,
                                companyType: null,
                                heading: null,
                                commercialInConfidence: null,
                                hidden: null,
                                textCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('page-details', null, { reload: 'page-details' });
                }, function() {
                    $state.go('page-details');
                });
            }]
        })
        .state('page-details.edit', {
            parent: 'page-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-details/page-details-dialog.html',
                    controller: 'PageDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PageDetails', function(PageDetails) {
                            return PageDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page-details', null, { reload: 'page-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page-details.delete', {
            parent: 'page-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-details/page-details-delete-dialog.html',
                    controller: 'PageDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PageDetails', function(PageDetails) {
                            return PageDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page-details', null, { reload: 'page-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
