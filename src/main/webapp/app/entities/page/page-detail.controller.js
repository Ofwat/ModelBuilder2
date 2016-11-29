(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDetailController', PageDetailController);

    PageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Page', 'PageDetails', 'Section', 'ModelBuilderDocument', 'Model'];

    function PageDetailController($scope, $rootScope, $stateParams, previousState, entity, Page, PageDetails, Section, ModelBuilderDocument, Model) {
        var vm = this;

        vm.page = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:pageUpdate', function(event, result) {
            vm.page = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
