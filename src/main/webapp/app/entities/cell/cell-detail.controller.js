(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellDetailController', CellDetailController);

    CellDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cell', 'Line'];

    function CellDetailController($scope, $rootScope, $stateParams, previousState, entity, Cell, Line) {
        var vm = this;

        vm.cell = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:cellUpdate', function(event, result) {
            vm.cell = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
