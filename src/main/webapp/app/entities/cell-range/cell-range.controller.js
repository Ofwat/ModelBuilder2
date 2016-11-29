(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellRangeController', CellRangeController);

    CellRangeController.$inject = ['$scope', '$state', 'CellRange', 'CellRangeSearch'];

    function CellRangeController ($scope, $state, CellRange, CellRangeSearch) {
        var vm = this;
        
        vm.cellRanges = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CellRange.query(function(result) {
                vm.cellRanges = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CellRangeSearch.query({query: vm.searchQuery}, function(result) {
                vm.cellRanges = result;
            });
        }    }
})();
