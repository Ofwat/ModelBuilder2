(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('text-block', {
            parent: 'entity',
            url: '/text-block',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TextBlocks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/text-block/text-blocks.html',
                    controller: 'TextBlockController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('text-block-detail', {
            parent: 'entity',
            url: '/text-block/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TextBlock'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/text-block/text-block-detail.html',
                    controller: 'TextBlockDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TextBlock', function($stateParams, TextBlock) {
                    return TextBlock.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'text-block',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('text-block-detail.edit', {
            parent: 'text-block-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text-block/text-block-dialog.html',
                    controller: 'TextBlockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TextBlock', function(TextBlock) {
                            return TextBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('text-block.new', {
            parent: 'text-block',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text-block/text-block-dialog.html',
                    controller: 'TextBlockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                versionNumber: null,
                                textFormatCode: null,
                                textTypeCode: null,
                                retired: null,
                                data: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('text-block', null, { reload: 'text-block' });
                }, function() {
                    $state.go('text-block');
                });
            }]
        })
        .state('text-block.edit', {
            parent: 'text-block',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text-block/text-block-dialog.html',
                    controller: 'TextBlockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TextBlock', function(TextBlock) {
                            return TextBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('text-block', null, { reload: 'text-block' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('text-block.delete', {
            parent: 'text-block',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/text-block/text-block-delete-dialog.html',
                    controller: 'TextBlockDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TextBlock', function(TextBlock) {
                            return TextBlock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('text-block', null, { reload: 'text-block' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
