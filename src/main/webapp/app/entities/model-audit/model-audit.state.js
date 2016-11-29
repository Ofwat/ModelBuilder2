(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-audit', {
            parent: 'entity',
            url: '/model-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ModelAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-audit/model-audits.html',
                    controller: 'ModelAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('model-audit-detail', {
            parent: 'entity',
            url: '/model-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ModelAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-audit/model-audit-detail.html',
                    controller: 'ModelAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ModelAudit', function($stateParams, ModelAudit) {
                    return ModelAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-audit-detail.edit', {
            parent: 'model-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-audit/model-audit-dialog.html',
                    controller: 'ModelAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelAudit', function(ModelAudit) {
                            return ModelAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-audit.new', {
            parent: 'model-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-audit/model-audit-dialog.html',
                    controller: 'ModelAuditDialogController',
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
                    $state.go('model-audit', null, { reload: 'model-audit' });
                }, function() {
                    $state.go('model-audit');
                });
            }]
        })
        .state('model-audit.edit', {
            parent: 'model-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-audit/model-audit-dialog.html',
                    controller: 'ModelAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelAudit', function(ModelAudit) {
                            return ModelAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-audit', null, { reload: 'model-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-audit.delete', {
            parent: 'model-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-audit/model-audit-delete-dialog.html',
                    controller: 'ModelAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelAudit', function(ModelAudit) {
                            return ModelAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-audit', null, { reload: 'model-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
