(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('heading', {
            parent: 'entity',
            url: '/heading',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Headings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/heading/headings.html',
                    controller: 'HeadingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('heading-detail', {
            parent: 'entity',
            url: '/heading/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Heading'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/heading/heading-detail.html',
                    controller: 'HeadingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Heading', function($stateParams, Heading) {
                    return Heading.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'heading',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('heading-detail.edit', {
            parent: 'heading-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/heading/heading-dialog.html',
                    controller: 'HeadingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Heading', function(Heading) {
                            return Heading.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('heading.new', {
            parent: 'heading',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/heading/heading-dialog.html',
                    controller: 'HeadingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                notes: null,
                                parent: null,
                                annotation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('heading', null, { reload: 'heading' });
                }, function() {
                    $state.go('heading');
                });
            }]
        })
        .state('heading.edit', {
            parent: 'heading',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/heading/heading-dialog.html',
                    controller: 'HeadingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Heading', function(Heading) {
                            return Heading.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('heading', null, { reload: 'heading' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('heading.delete', {
            parent: 'heading',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/heading/heading-delete-dialog.html',
                    controller: 'HeadingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Heading', function(Heading) {
                            return Heading.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('heading', null, { reload: 'heading' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
