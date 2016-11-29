(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDetailsDetailController', ModelDetailsDetailController);

    ModelDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelDetails'];

    function ModelDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelDetails) {
        var vm = this;

        vm.modelDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:modelDetailsUpdate', function(event, result) {
            vm.modelDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
