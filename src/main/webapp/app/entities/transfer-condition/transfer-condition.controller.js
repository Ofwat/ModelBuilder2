(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferConditionController', TransferConditionController);

    TransferConditionController.$inject = ['$scope', '$state', 'TransferCondition', 'TransferConditionSearch'];

    function TransferConditionController ($scope, $state, TransferCondition, TransferConditionSearch) {
        var vm = this;
        
        vm.transferConditions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TransferCondition.query(function(result) {
                vm.transferConditions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransferConditionSearch.query({query: vm.searchQuery}, function(result) {
                vm.transferConditions = result;
            });
        }    }
})();
