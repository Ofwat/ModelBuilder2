(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDetailsDetailController', PageDetailsDetailController);

    PageDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PageDetails'];

    function PageDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, PageDetails) {
        var vm = this;

        vm.pageDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:pageDetailsUpdate', function(event, result) {
            vm.pageDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
