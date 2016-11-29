(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDetailController', LineDetailController);

    LineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Line', 'LineDetails', 'CellRange', 'Cell', 'ModelBuilderDocument', 'Section'];

    function LineDetailController($scope, $rootScope, $stateParams, previousState, entity, Line, LineDetails, CellRange, Cell, ModelBuilderDocument, Section) {
        var vm = this;

        vm.line = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:lineUpdate', function(event, result) {
            vm.line = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
