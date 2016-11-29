(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextDetailController', TextDetailController);

    TextDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Text', 'TextBlock', 'Model'];

    function TextDetailController($scope, $rootScope, $stateParams, previousState, entity, Text, TextBlock, Model) {
        var vm = this;

        vm.text = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:textUpdate', function(event, result) {
            vm.text = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
