(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer-block', {
            parent: 'entity',
            url: '/transfer-block',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlocks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block/transfer-blocks.html',
                    controller: 'TransferBlockController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transfer-block-detail', {
            parent: 'entity',
            url: '/transfer-block/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlock'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block/transfer-block-detail.html',
                    controller: 'TransferBlockDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransferBlock', function($stateParams, TransferBlock) {
                    return TransferBlock.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer-block',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-block-detail.edit', {
            parent: 'transfer-block-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block/transfer-block-dialog.html',
                    controller: 'TransferBlockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlock', function(TransferBlock) {
                            return TransferBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block.new', {
            parent: 'transfer-block',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block/transfer-block-dialog.html',
                    controller: 'TransferBlockDialogController',
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
                    $state.go('transfer-block', null, { reload: 'transfer-block' });
                }, function() {
                    $state.go('transfer-block');
                });
            }]
        })
        .state('transfer-block.edit', {
            parent: 'transfer-block',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block/transfer-block-dialog.html',
                    controller: 'TransferBlockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlock', function(TransferBlock) {
                            return TransferBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block', null, { reload: 'transfer-block' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block.delete', {
            parent: 'transfer-block',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block/transfer-block-delete-dialog.html',
                    controller: 'TransferBlockDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransferBlock', function(TransferBlock) {
                            return TransferBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block', null, { reload: 'transfer-block' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
