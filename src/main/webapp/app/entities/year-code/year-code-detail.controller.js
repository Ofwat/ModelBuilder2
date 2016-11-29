(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearCodeDetailController', YearCodeDetailController);

    YearCodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'YearCode', 'TransferBlockItem'];

    function YearCodeDetailController($scope, $rootScope, $stateParams, previousState, entity, YearCode, TransferBlockItem) {
        var vm = this;

        vm.yearCode = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:yearCodeUpdate', function(event, result) {
            vm.yearCode = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
