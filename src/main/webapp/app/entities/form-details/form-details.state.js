(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('form-details', {
            parent: 'entity',
            url: '/form-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-details/form-details.html',
                    controller: 'FormDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('form-details-detail', {
            parent: 'entity',
            url: '/form-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FormDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form-details/form-details-detail.html',
                    controller: 'FormDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FormDetails', function($stateParams, FormDetails) {
                    return FormDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'form-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('form-details-detail.edit', {
            parent: 'form-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-details/form-details-dialog.html',
                    controller: 'FormDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormDetails', function(FormDetails) {
                            return FormDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-details.new', {
            parent: 'form-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-details/form-details-dialog.html',
                    controller: 'FormDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                companyType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('form-details', null, { reload: 'form-details' });
                }, function() {
                    $state.go('form-details');
                });
            }]
        })
        .state('form-details.edit', {
            parent: 'form-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-details/form-details-dialog.html',
                    controller: 'FormDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormDetails', function(FormDetails) {
                            return FormDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-details', null, { reload: 'form-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form-details.delete', {
            parent: 'form-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form-details/form-details-delete-dialog.html',
                    controller: 'FormDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormDetails', function(FormDetails) {
                            return FormDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form-details', null, { reload: 'form-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
