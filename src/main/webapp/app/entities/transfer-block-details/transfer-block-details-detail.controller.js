(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDetailsDetailController', TransferBlockDetailsDetailController);

    TransferBlockDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransferBlockDetails'];

    function TransferBlockDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, TransferBlockDetails) {
        var vm = this;

        vm.transferBlockDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:transferBlockDetailsUpdate', function(event, result) {
            vm.transferBlockDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
