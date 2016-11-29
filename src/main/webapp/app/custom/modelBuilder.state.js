(function() {

'use strict';

angular.module('modelBuilderApp')
    .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider){
        $stateProvider
            .state('modelBuilder', {
                parent: 'model',
                url: '/modelBuilder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modelBuilderApp.modelBuilder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/custom/modelBuilder.html',
                        controller: 'ModelBuilderController'
                    }
                },
                resolve: {
                    /*
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('modelBuilder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    */
                    entity: ['$stateParams', 'Model', function($stateParams, Model) {
                        return Model.get({id : $stateParams.id});
                    }]
                }
            })
            .state('loadModel', {
                parent: 'model',
                url: '/loadModel',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modelBuilderApp.modelBuilder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/custom/loadModel.html',
                        controller: 'LoadModelController'
                    }
                },
                resolve: {
                    /*
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('modelBuilder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    */
                    entity: ['$stateParams', 'Model', function($stateParams, Model) {
                        return null;
                    }]
                }
            })
            .state('modelBuilder.copy', {
                parent: 'modelBuilder',
                url: '/copy',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/custom/copyModel-dialog.html',
                        controller: 'CopyModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ModelDetails', function(ModelDetails) {
                                return ModelDetails.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('modelBuilder', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        })
                }]
            })
    }
})();
