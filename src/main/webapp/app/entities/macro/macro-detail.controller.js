(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('MacroDetailController', MacroDetailController);

    MacroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Macro', 'Model'];

    function MacroDetailController($scope, $rootScope, $stateParams, previousState, entity, Macro, Model) {
        var vm = this;

        vm.macro = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:macroUpdate', function(event, result) {
            vm.macro = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
