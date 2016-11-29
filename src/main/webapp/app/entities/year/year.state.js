(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('year', {
            parent: 'entity',
            url: '/year',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Years'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/year/years.html',
                    controller: 'YearController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('year-detail', {
            parent: 'entity',
            url: '/year/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Year'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/year/year-detail.html',
                    controller: 'YearDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Year', function($stateParams, Year) {
                    return Year.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'year',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('year-detail.edit', {
            parent: 'year-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year/year-dialog.html',
                    controller: 'YearDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Year', function(Year) {
                            return Year.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('year.new', {
            parent: 'year',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year/year-dialog.html',
                    controller: 'YearDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                base: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('year', null, { reload: 'year' });
                }, function() {
                    $state.go('year');
                });
            }]
        })
        .state('year.edit', {
            parent: 'year',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year/year-dialog.html',
                    controller: 'YearDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Year', function(Year) {
                            return Year.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('year', null, { reload: 'year' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('year.delete', {
            parent: 'year',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year/year-delete-dialog.html',
                    controller: 'YearDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Year', function(Year) {
                            return Year.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('year', null, { reload: 'year' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
