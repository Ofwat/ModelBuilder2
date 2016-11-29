(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellController', CellController);

    CellController.$inject = ['$scope', '$state', 'Cell', 'CellSearch'];

    function CellController ($scope, $state, Cell, CellSearch) {
        var vm = this;
        
        vm.cells = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cell.query(function(result) {
                vm.cells = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CellSearch.query({query: vm.searchQuery}, function(result) {
                vm.cells = result;
            });
        }    }
})();
