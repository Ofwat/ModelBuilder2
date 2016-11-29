(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDetailsController', TransferBlockDetailsController);

    TransferBlockDetailsController.$inject = ['$scope', '$state', 'TransferBlockDetails', 'TransferBlockDetailsSearch'];

    function TransferBlockDetailsController ($scope, $state, TransferBlockDetails, TransferBlockDetailsSearch) {
        var vm = this;
        
        vm.transferBlockDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TransferBlockDetails.query(function(result) {
                vm.transferBlockDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TransferBlockDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.transferBlockDetails = result;
            });
        }    }
})();
