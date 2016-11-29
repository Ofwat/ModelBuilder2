(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormHeadingCellDetailController', FormHeadingCellDetailController);

    FormHeadingCellDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormHeadingCell', 'Form'];

    function FormHeadingCellDetailController($scope, $rootScope, $stateParams, previousState, entity, FormHeadingCell, Form) {
        var vm = this;

        vm.formHeadingCell = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:formHeadingCellUpdate', function(event, result) {
            vm.formHeadingCell = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
