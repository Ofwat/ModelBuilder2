(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferController', TransferController);

    TransferController.$inject = ['$scope', '$state', 'Transfer', 'TransferSearch'];

    function TransferController ($scope, $state, Transfer, TransferSearch) {
        var vm = this;
        
        vm.transfers = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Transfer.query(function(result) {
                vm.transfers = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransferSearch.query({query: vm.searchQuery}, function(result) {
                vm.transfers = result;
            });
        }    }
})();
