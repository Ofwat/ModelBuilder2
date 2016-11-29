(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audit-details', {
            parent: 'entity',
            url: '/audit-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuditDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-details/audit-details.html',
                    controller: 'AuditDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('audit-details-detail', {
            parent: 'entity',
            url: '/audit-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AuditDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-details/audit-details-detail.html',
                    controller: 'AuditDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AuditDetails', function($stateParams, AuditDetails) {
                    return AuditDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'audit-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('audit-details-detail.edit', {
            parent: 'audit-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-details/audit-details-dialog.html',
                    controller: 'AuditDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditDetails', function(AuditDetails) {
                            return AuditDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-details.new', {
            parent: 'audit-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-details/audit-details-dialog.html',
                    controller: 'AuditDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                username: null,
                                timestamp: null,
                                reason: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('audit-details', null, { reload: 'audit-details' });
                }, function() {
                    $state.go('audit-details');
                });
            }]
        })
        .state('audit-details.edit', {
            parent: 'audit-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-details/audit-details-dialog.html',
                    controller: 'AuditDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditDetails', function(AuditDetails) {
                            return AuditDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-details', null, { reload: 'audit-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-details.delete', {
            parent: 'audit-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-details/audit-details-delete-dialog.html',
                    controller: 'AuditDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuditDetails', function(AuditDetails) {
                            return AuditDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-details', null, { reload: 'audit-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
