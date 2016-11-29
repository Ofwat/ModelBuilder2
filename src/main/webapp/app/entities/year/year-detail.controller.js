(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearDetailController', YearDetailController);

    YearDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Year', 'Model'];

    function YearDetailController($scope, $rootScope, $stateParams, previousState, entity, Year, Model) {
        var vm = this;

        vm.year = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:yearUpdate', function(event, result) {
            vm.year = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
