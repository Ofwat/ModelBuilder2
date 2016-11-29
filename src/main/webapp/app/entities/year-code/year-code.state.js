(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('year-code', {
            parent: 'entity',
            url: '/year-code',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'YearCodes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/year-code/year-codes.html',
                    controller: 'YearCodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('year-code-detail', {
            parent: 'entity',
            url: '/year-code/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'YearCode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/year-code/year-code-detail.html',
                    controller: 'YearCodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'YearCode', function($stateParams, YearCode) {
                    return YearCode.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'year-code',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('year-code-detail.edit', {
            parent: 'year-code-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year-code/year-code-dialog.html',
                    controller: 'YearCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['YearCode', function(YearCode) {
                            return YearCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('year-code.new', {
            parent: 'year-code',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year-code/year-code-dialog.html',
                    controller: 'YearCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                yearCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('year-code', null, { reload: 'year-code' });
                }, function() {
                    $state.go('year-code');
                });
            }]
        })
        .state('year-code.edit', {
            parent: 'year-code',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year-code/year-code-dialog.html',
                    controller: 'YearCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['YearCode', function(YearCode) {
                            return YearCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('year-code', null, { reload: 'year-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('year-code.delete', {
            parent: 'year-code',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/year-code/year-code-delete-dialog.html',
                    controller: 'YearCodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['YearCode', function(YearCode) {
                            return YearCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('year-code', null, { reload: 'year-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
