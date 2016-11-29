(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ItemDetailController', ItemDetailController);

    ItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Item', 'Model'];

    function ItemDetailController($scope, $rootScope, $stateParams, previousState, entity, Item, Model) {
        var vm = this;

        vm.item = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:itemUpdate', function(event, result) {
            vm.item = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
