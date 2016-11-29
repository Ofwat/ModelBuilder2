(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer-block-item', {
            parent: 'entity',
            url: '/transfer-block-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlockItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-items.html',
                    controller: 'TransferBlockItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transfer-block-item-detail', {
            parent: 'entity',
            url: '/transfer-block-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransferBlockItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-item-detail.html',
                    controller: 'TransferBlockItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransferBlockItem', function($stateParams, TransferBlockItem) {
                    return TransferBlockItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer-block-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-block-item-detail.edit', {
            parent: 'transfer-block-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-item-dialog.html',
                    controller: 'TransferBlockItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlockItem', function(TransferBlockItem) {
                            return TransferBlockItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block-item.new', {
            parent: 'transfer-block-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-item-dialog.html',
                    controller: 'TransferBlockItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemCode: null,
                                companyType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfer-block-item', null, { reload: 'transfer-block-item' });
                }, function() {
                    $state.go('transfer-block-item');
                });
            }]
        })
        .state('transfer-block-item.edit', {
            parent: 'transfer-block-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-item-dialog.html',
                    controller: 'TransferBlockItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransferBlockItem', function(TransferBlockItem) {
                            return TransferBlockItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block-item', null, { reload: 'transfer-block-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer-block-item.delete', {
            parent: 'transfer-block-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer-block-item/transfer-block-item-delete-dialog.html',
                    controller: 'TransferBlockItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransferBlockItem', function(TransferBlockItem) {
                            return TransferBlockItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer-block-item', null, { reload: 'transfer-block-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
