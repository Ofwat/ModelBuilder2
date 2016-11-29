(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('InputDetailController', InputDetailController);

    InputDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Input', 'Model'];

    function InputDetailController($scope, $rootScope, $stateParams, previousState, entity, Input, Model) {
        var vm = this;

        vm.input = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:inputUpdate', function(event, result) {
            vm.input = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
