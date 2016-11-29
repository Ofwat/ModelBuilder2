(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferDetailController', TransferDetailController);

    TransferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transfer', 'TransferCondition', 'TransferBlock', 'Model'];

    function TransferDetailController($scope, $rootScope, $stateParams, previousState, entity, Transfer, TransferCondition, TransferBlock, Model) {
        var vm = this;

        vm.transfer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:transferUpdate', function(event, result) {
            vm.transfer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
