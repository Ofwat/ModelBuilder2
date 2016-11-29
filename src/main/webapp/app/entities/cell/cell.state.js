(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cell', {
            parent: 'entity',
            url: '/cell',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cells'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cell/cells.html',
                    controller: 'CellController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('cell-detail', {
            parent: 'entity',
            url: '/cell/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cell'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cell/cell-detail.html',
                    controller: 'CellDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Cell', function($stateParams, Cell) {
                    return Cell.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cell',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cell-detail.edit', {
            parent: 'cell-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell/cell-dialog.html',
                    controller: 'CellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cell', function(Cell) {
                            return Cell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cell.new', {
            parent: 'cell',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell/cell-dialog.html',
                    controller: 'CellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                year: null,
                                equation: null,
                                type: null,
                                cgType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cell', null, { reload: 'cell' });
                }, function() {
                    $state.go('cell');
                });
            }]
        })
        .state('cell.edit', {
            parent: 'cell',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell/cell-dialog.html',
                    controller: 'CellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cell', function(Cell) {
                            return Cell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cell', null, { reload: 'cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cell.delete', {
            parent: 'cell',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell/cell-delete-dialog.html',
                    controller: 'CellDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cell', function(Cell) {
                            return Cell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cell', null, { reload: 'cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
