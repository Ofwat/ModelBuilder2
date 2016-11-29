(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('form-cell', {
            parent: 'entity',
            url: '/form-cell',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormCells'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-cell/form-cells.html',
                    controller: 'FormCellController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('form-cell-detail', {
            parent: 'entity',
            url: '/form-cell/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormCell'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-cell/form-cell-detail.html',
                    controller: 'FormCellDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FormCell', function($stateParams, FormCell) {
                    return FormCell.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'form-cell',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('form-cell-detail.edit', {
            parent: 'form-cell-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-cell/form-cell-dialog.html',
                    controller: 'FormCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormCell', function(FormCell) {
                            return FormCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-cell.new', {
            parent: 'form-cell',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-cell/form-cell-dialog.html',
                    controller: 'FormCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cellCode: null,
                                useConfidenceGrade: null,
                                inputConfidenceGrade: null,
                                confidenceGradeInputCode: null,
                                rowHeadingSource: null,
                                columnHeadingSource: null,
                                groupDescriptionCode: null,
                                row: null,
                                formColumn: null,
                                rowSpan: null,
                                formColumnSpan: null,
                                width: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('form-cell', null, { reload: 'form-cell' });
                }, function() {
                    $state.go('form-cell');
                });
            }]
        })
        .state('form-cell.edit', {
            parent: 'form-cell',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-cell/form-cell-dialog.html',
                    controller: 'FormCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormCell', function(FormCell) {
                            return FormCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-cell', null, { reload: 'form-cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-cell.delete', {
            parent: 'form-cell',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-cell/form-cell-delete-dialog.html',
                    controller: 'FormCellDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormCell', function(FormCell) {
                            return FormCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-cell', null, { reload: 'form-cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
