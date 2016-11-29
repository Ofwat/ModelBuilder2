(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-builder-document', {
            parent: 'entity',
            url: '/model-builder-document',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ModelBuilderDocuments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-builder-document/model-builder-documents.html',
                    controller: 'ModelBuilderDocumentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('model-builder-document-detail', {
            parent: 'entity',
            url: '/model-builder-document/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ModelBuilderDocument'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-builder-document/model-builder-document-detail.html',
                    controller: 'ModelBuilderDocumentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ModelBuilderDocument', function($stateParams, ModelBuilderDocument) {
                    return ModelBuilderDocument.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-builder-document',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-builder-document-detail.edit', {
            parent: 'model-builder-document-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-builder-document/model-builder-document-dialog.html',
                    controller: 'ModelBuilderDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelBuilderDocument', function(ModelBuilderDocument) {
                            return ModelBuilderDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-builder-document.new', {
            parent: 'model-builder-document',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-builder-document/model-builder-document-dialog.html',
                    controller: 'ModelBuilderDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reporter: null,
                                auditor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-builder-document', null, { reload: 'model-builder-document' });
                }, function() {
                    $state.go('model-builder-document');
                });
            }]
        })
        .state('model-builder-document.edit', {
            parent: 'model-builder-document',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-builder-document/model-builder-document-dialog.html',
                    controller: 'ModelBuilderDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelBuilderDocument', function(ModelBuilderDocument) {
                            return ModelBuilderDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-builder-document', null, { reload: 'model-builder-document' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-builder-document.delete', {
            parent: 'model-builder-document',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-builder-document/model-builder-document-delete-dialog.html',
                    controller: 'ModelBuilderDocumentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelBuilderDocument', function(ModelBuilderDocument) {
                            return ModelBuilderDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-builder-document', null, { reload: 'model-builder-document' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
