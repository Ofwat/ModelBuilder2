(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cell-range', {
            parent: 'entity',
            url: '/cell-range',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CellRanges'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cell-range/cell-ranges.html',
                    controller: 'CellRangeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('cell-range-detail', {
            parent: 'entity',
            url: '/cell-range/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CellRange'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cell-range/cell-range-detail.html',
                    controller: 'CellRangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CellRange', function($stateParams, CellRange) {
                    return CellRange.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cell-range',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cell-range-detail.edit', {
            parent: 'cell-range-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell-range/cell-range-dialog.html',
                    controller: 'CellRangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CellRange', function(CellRange) {
                            return CellRange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cell-range.new', {
            parent: 'cell-range',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell-range/cell-range-dialog.html',
                    controller: 'CellRangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startYear: null,
                                endYear: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cell-range', null, { reload: 'cell-range' });
                }, function() {
                    $state.go('cell-range');
                });
            }]
        })
        .state('cell-range.edit', {
            parent: 'cell-range',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell-range/cell-range-dialog.html',
                    controller: 'CellRangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CellRange', function(CellRange) {
                            return CellRange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cell-range', null, { reload: 'cell-range' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cell-range.delete', {
            parent: 'cell-range',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cell-range/cell-range-delete-dialog.html',
                    controller: 'CellRangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CellRange', function(CellRange) {
                            return CellRange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cell-range', null, { reload: 'cell-range' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
