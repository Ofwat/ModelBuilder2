(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockController', TransferBlockController);

    TransferBlockController.$inject = ['$scope', '$state', 'TransferBlock', 'TransferBlockSearch'];

    function TransferBlockController ($scope, $state, TransferBlock, TransferBlockSearch) {
        var vm = this;
        
        vm.transferBlocks = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TransferBlock.query(function(result) {
                vm.transferBlocks = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransferBlockSearch.query({query: vm.searchQuery}, function(result) {
                vm.transferBlocks = result;
            });
        }    }
})();
