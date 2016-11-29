(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellRangeDetailController', CellRangeDetailController);

    CellRangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CellRange'];

    function CellRangeDetailController($scope, $rootScope, $stateParams, previousState, entity, CellRange) {
        var vm = this;

        vm.cellRange = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:cellRangeUpdate', function(event, result) {
            vm.cellRange = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
