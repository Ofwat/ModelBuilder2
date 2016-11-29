(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDetailController', SectionDetailController);

    SectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Section', 'SectionDetails', 'ModelBuilderDocument', 'Line', 'Form', 'Page'];

    function SectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Section, SectionDetails, ModelBuilderDocument, Line, Form, Page) {
        var vm = this;

        vm.section = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:sectionUpdate', function(event, result) {
            vm.section = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
