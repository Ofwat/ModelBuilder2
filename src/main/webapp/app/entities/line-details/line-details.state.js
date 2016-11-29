(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('line-details', {
            parent: 'entity',
            url: '/line-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LineDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/line-details/line-details.html',
                    controller: 'LineDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('line-details-detail', {
            parent: 'entity',
            url: '/line-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LineDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/line-details/line-details-detail.html',
                    controller: 'LineDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'LineDetails', function($stateParams, LineDetails) {
                    return LineDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'line-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('line-details-detail.edit', {
            parent: 'line-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line-details/line-details-dialog.html',
                    controller: 'LineDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LineDetails', function(LineDetails) {
                            return LineDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('line-details.new', {
            parent: 'line-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line-details/line-details-dialog.html',
                    controller: 'LineDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                heading: null,
                                code: null,
                                description: null,
                                equation: null,
                                lineNumber: null,
                                ruleText: null,
                                type: null,
                                companyType: null,
                                useConfidenceGrade: null,
                                validationRuleCode: null,
                                textCode: null,
                                unit: null,
                                decimalPlaces: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('line-details', null, { reload: 'line-details' });
                }, function() {
                    $state.go('line-details');
                });
            }]
        })
        .state('line-details.edit', {
            parent: 'line-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line-details/line-details-dialog.html',
                    controller: 'LineDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LineDetails', function(LineDetails) {
                            return LineDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('line-details', null, { reload: 'line-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('line-details.delete', {
            parent: 'line-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/line-details/line-details-delete-dialog.html',
                    controller: 'LineDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LineDetails', function(LineDetails) {
                            return LineDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('line-details', null, { reload: 'line-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
