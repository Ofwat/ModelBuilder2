(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CompanyPageDetailController', CompanyPageDetailController);

    CompanyPageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyPage', 'Model'];

    function CompanyPageDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyPage, Model) {
        var vm = this;

        vm.companyPage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:companyPageUpdate', function(event, result) {
            vm.companyPage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
