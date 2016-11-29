(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockItemController', TransferBlockItemController);

    TransferBlockItemController.$inject = ['$scope', '$state', 'TransferBlockItem', 'TransferBlockItemSearch'];

    function TransferBlockItemController ($scope, $state, TransferBlockItem, TransferBlockItemSearch) {
        var vm = this;
        
        vm.transferBlockItems = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TransferBlockItem.query(function(result) {
                vm.transferBlockItems = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransferBlockItemSearch.query({query: vm.searchQuery}, function(result) {
                vm.transferBlockItems = result;
            });
        }    }
})();
