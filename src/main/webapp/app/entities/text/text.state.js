(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('text', {
            parent: 'entity',
            url: '/text',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Texts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/text/texts.html',
                    controller: 'TextController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('text-detail', {
            parent: 'entity',
            url: '/text/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Text'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/text/text-detail.html',
                    controller: 'TextDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Text', function($stateParams, Text) {
                    return Text.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'text',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('text-detail.edit', {
            parent: 'text-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text/text-dialog.html',
                    controller: 'TextDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Text', function(Text) {
                            return Text.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('text.new', {
            parent: 'text',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text/text-dialog.html',
                    controller: 'TextDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('text', null, { reload: 'text' });
                }, function() {
                    $state.go('text');
                });
            }]
        })
        .state('text.edit', {
            parent: 'text',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text/text-dialog.html',
                    controller: 'TextDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Text', function(Text) {
                            return Text.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('text', null, { reload: 'text' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('text.delete', {
            parent: 'text',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text/text-delete-dialog.html',
                    controller: 'TextDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Text', function(Text) {
                            return Text.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('text', null, { reload: 'text' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
