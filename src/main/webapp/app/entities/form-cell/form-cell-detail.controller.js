(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormCellDetailController', FormCellDetailController);

    FormCellDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormCell', 'Form'];

    function FormCellDetailController($scope, $rootScope, $stateParams, previousState, entity, FormCell, Form) {
        var vm = this;

        vm.formCell = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:formCellUpdate', function(event, result) {
            vm.formCell = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
