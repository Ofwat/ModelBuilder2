(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockItemDetailController', TransferBlockItemDetailController);

    TransferBlockItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransferBlockItem', 'YearCode', 'TransferBlock'];

    function TransferBlockItemDetailController($scope, $rootScope, $stateParams, previousState, entity, TransferBlockItem, YearCode, TransferBlock) {
        var vm = this;

        vm.transferBlockItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:transferBlockItemUpdate', function(event, result) {
            vm.transferBlockItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
