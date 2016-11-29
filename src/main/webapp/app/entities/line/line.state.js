(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('line', {
            parent: 'entity',
            url: '/line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Lines'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/line/lines.html',
                    controller: 'LineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('line-detail', {
            parent: 'entity',
            url: '/line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Line'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/line/line-detail.html',
                    controller: 'LineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Line', function($stateParams, Line) {
                    return Line.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'line',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('line-detail.edit', {
            parent: 'line-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line/line-dialog.html',
                    controller: 'LineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Line', function(Line) {
                            return Line.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('line.new', {
            parent: 'line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line/line-dialog.html',
                    controller: 'LineDialogController',
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
                    $state.go('line', null, { reload: 'line' });
                }, function() {
                    $state.go('line');
                });
            }]
        })
        .state('line.edit', {
            parent: 'line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line/line-dialog.html',
                    controller: 'LineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Line', function(Line) {
                            return Line.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('line', null, { reload: 'line' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('line.delete', {
            parent: 'line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line/line-delete-dialog.html',
                    controller: 'LineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Line', function(Line) {
                            return Line.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('line', null, { reload: 'line' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
