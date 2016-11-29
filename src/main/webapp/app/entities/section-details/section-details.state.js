(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('section-details', {
            parent: 'entity',
            url: '/section-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SectionDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/section-details/section-details.html',
                    controller: 'SectionDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('section-details-detail', {
            parent: 'entity',
            url: '/section-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SectionDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/section-details/section-details-detail.html',
                    controller: 'SectionDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SectionDetails', function($stateParams, SectionDetails) {
                    return SectionDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'section-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('section-details-detail.edit', {
            parent: 'section-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section-details/section-details-dialog.html',
                    controller: 'SectionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SectionDetails', function(SectionDetails) {
                            return SectionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('section-details.new', {
            parent: 'section-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section-details/section-details-dialog.html',
                    controller: 'SectionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                display: null,
                                code: null,
                                groupType: null,
                                useLineNumber: false,
                                useItemCode: false,
                                useItemDescription: false,
                                useUnit: false,
                                useYearCode: false,
                                useConfidenceGrades: false,
                                allowGroupSelection: null,
                                allowDataChanges: null,
                                sectionType: null,
                                itemCodeColumn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('section-details', null, { reload: 'section-details' });
                }, function() {
                    $state.go('section-details');
                });
            }]
        })
        .state('section-details.edit', {
            parent: 'section-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section-details/section-details-dialog.html',
                    controller: 'SectionDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SectionDetails', function(SectionDetails) {
                            return SectionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('section-details', null, { reload: 'section-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('section-details.delete', {
            parent: 'section-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/section-details/section-details-delete-dialog.html',
                    controller: 'SectionDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SectionDetails', function(SectionDetails) {
                            return SectionDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('section-details', null, { reload: 'section-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
