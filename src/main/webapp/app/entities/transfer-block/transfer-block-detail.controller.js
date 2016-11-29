(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDetailController', TransferBlockDetailController);

    TransferBlockDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransferBlock', 'TransferBlockDetails', 'TransferBlockItem', 'Transfer'];

    function TransferBlockDetailController($scope, $rootScope, $stateParams, previousState, entity, TransferBlock, TransferBlockDetails, TransferBlockItem, Transfer) {
        var vm = this;

        vm.transferBlock = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:transferBlockUpdate', function(event, result) {
            vm.transferBlock = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
