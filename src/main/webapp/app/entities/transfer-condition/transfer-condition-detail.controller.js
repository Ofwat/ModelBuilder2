(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferConditionDetailController', TransferConditionDetailController);

    TransferConditionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransferCondition'];

    function TransferConditionDetailController($scope, $rootScope, $stateParams, previousState, entity, TransferCondition) {
        var vm = this;

        vm.transferCondition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:transferConditionUpdate', function(event, result) {
            vm.transferCondition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
