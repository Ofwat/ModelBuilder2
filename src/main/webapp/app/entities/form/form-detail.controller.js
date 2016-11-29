(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDetailController', FormDetailController);

    FormDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Form', 'FormDetails', 'FormCell', 'FormHeadingCell', 'Section'];

    function FormDetailController($scope, $rootScope, $stateParams, previousState, entity, Form, FormDetails, FormCell, FormHeadingCell, Section) {
        var vm = this;

        vm.form = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:formUpdate', function(event, result) {
            vm.form = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
