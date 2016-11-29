(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('form', {
            parent: 'entity',
            url: '/form',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Forms'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form/forms.html',
                    controller: 'FormController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('form-detail', {
            parent: 'entity',
            url: '/form/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Form'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/form/form-detail.html',
                    controller: 'FormDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Form', function($stateParams, Form) {
                    return Form.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'form',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('form-detail.edit', {
            parent: 'form-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form/form-dialog.html',
                    controller: 'FormDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Form', function(Form) {
                            return Form.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form.new', {
            parent: 'form',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form/form-dialog.html',
                    controller: 'FormDialogController',
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
                    $state.go('form', null, { reload: 'form' });
                }, function() {
                    $state.go('form');
                });
            }]
        })
        .state('form.edit', {
            parent: 'form',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form/form-dialog.html',
                    controller: 'FormDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Form', function(Form) {
                            return Form.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form', null, { reload: 'form' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('form.delete', {
            parent: 'form',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/form/form-delete-dialog.html',
                    controller: 'FormDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Form', function(Form) {
                            return Form.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('form', null, { reload: 'form' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
