(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('form-heading-cell', {
            parent: 'entity',
            url: '/form-heading-cell',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormHeadingCells'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cells.html',
                    controller: 'FormHeadingCellController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('form-heading-cell-detail', {
            parent: 'entity',
            url: '/form-heading-cell/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormHeadingCell'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cell-detail.html',
                    controller: 'FormHeadingCellDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FormHeadingCell', function($stateParams, FormHeadingCell) {
                    return FormHeadingCell.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'form-heading-cell',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('form-heading-cell-detail.edit', {
            parent: 'form-heading-cell-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cell-dialog.html',
                    controller: 'FormHeadingCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormHeadingCell', function(FormHeadingCell) {
                            return FormHeadingCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-heading-cell.new', {
            parent: 'form-heading-cell',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cell-dialog.html',
                    controller: 'FormHeadingCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                useLineNumber: null,
                                useItemCode: null,
                                useItemDescription: null,
                                useUnit: null,
                                useYearCode: null,
                                useConfidenceGrades: null,
                                row: null,
                                formHeadingColumn: null,
                                rowSpan: null,
                                formHeadingColumnSpan: null,
                                itemCode: null,
                                yearCode: null,
                                width: null,
                                cellCode: null,
                                groupDescriptionCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('form-heading-cell', null, { reload: 'form-heading-cell' });
                }, function() {
                    $state.go('form-heading-cell');
                });
            }]
        })
        .state('form-heading-cell.edit', {
            parent: 'form-heading-cell',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cell-dialog.html',
                    controller: 'FormHeadingCellDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormHeadingCell', function(FormHeadingCell) {
                            return FormHeadingCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-heading-cell', null, { reload: 'form-heading-cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-heading-cell.delete', {
            parent: 'form-heading-cell',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-heading-cell/form-heading-cell-delete-dialog.html',
                    controller: 'FormHeadingCellDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormHeadingCell', function(FormHeadingCell) {
                            return FormHeadingCell.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-heading-cell', null, { reload: 'form-heading-cell' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
