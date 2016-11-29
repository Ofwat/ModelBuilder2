(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextBlockDetailController', TextBlockDetailController);

    TextBlockDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TextBlock', 'Text'];

    function TextBlockDetailController($scope, $rootScope, $stateParams, previousState, entity, TextBlock, Text) {
        var vm = this;

        vm.textBlock = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:textBlockUpdate', function(event, result) {
            vm.textBlock = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
