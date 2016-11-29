(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDetailsDetailController', SectionDetailsDetailController);

    SectionDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SectionDetails'];

    function SectionDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, SectionDetails) {
        var vm = this;

        vm.sectionDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:sectionDetailsUpdate', function(event, result) {
            vm.sectionDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
