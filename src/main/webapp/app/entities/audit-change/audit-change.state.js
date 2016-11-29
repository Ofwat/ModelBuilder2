(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audit-change', {
            parent: 'entity',
            url: '/audit-change',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuditChanges'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-change/audit-changes.html',
                    controller: 'AuditChangeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('audit-change-detail', {
            parent: 'entity',
            url: '/audit-change/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuditChange'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-change/audit-change-detail.html',
                    controller: 'AuditChangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AuditChange', function($stateParams, AuditChange) {
                    return AuditChange.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'audit-change',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('audit-change-detail.edit', {
            parent: 'audit-change-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-change/audit-change-dialog.html',
                    controller: 'AuditChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditChange', function(AuditChange) {
                            return AuditChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-change.new', {
            parent: 'audit-change',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-change/audit-change-dialog.html',
                    controller: 'AuditChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                changeText: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('audit-change', null, { reload: 'audit-change' });
                }, function() {
                    $state.go('audit-change');
                });
            }]
        })
        .state('audit-change.edit', {
            parent: 'audit-change',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-change/audit-change-dialog.html',
                    controller: 'AuditChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditChange', function(AuditChange) {
                            return AuditChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-change', null, { reload: 'audit-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-change.delete', {
            parent: 'audit-change',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-change/audit-change-delete-dialog.html',
                    controller: 'AuditChangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuditChange', function(AuditChange) {
                            return AuditChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-change', null, { reload: 'audit-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
