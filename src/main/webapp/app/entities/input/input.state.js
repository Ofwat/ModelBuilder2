(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('input', {
            parent: 'entity',
            url: '/input',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Inputs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/input/inputs.html',
                    controller: 'InputController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('input-detail', {
            parent: 'entity',
            url: '/input/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Input'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/input/input-detail.html',
                    controller: 'InputDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Input', function($stateParams, Input) {
                    return Input.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'input',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('input-detail.edit', {
            parent: 'input-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input/input-dialog.html',
                    controller: 'InputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Input', function(Input) {
                            return Input.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('input.new', {
            parent: 'input',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input/input-dialog.html',
                    controller: 'InputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                tag: null,
                                version: null,
                                company: null,
                                defaultInput: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('input', null, { reload: 'input' });
                }, function() {
                    $state.go('input');
                });
            }]
        })
        .state('input.edit', {
            parent: 'input',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input/input-dialog.html',
                    controller: 'InputDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Input', function(Input) {
                            return Input.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('input', null, { reload: 'input' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('input.delete', {
            parent: 'input',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input/input-delete-dialog.html',
                    controller: 'InputDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Input', function(Input) {
                            return Input.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('input', null, { reload: 'input' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
