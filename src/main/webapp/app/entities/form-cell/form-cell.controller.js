(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormCellController', FormCellController);

    FormCellController.$inject = ['$scope', '$state', 'FormCell', 'FormCellSearch'];

    function FormCellController ($scope, $state, FormCell, FormCellSearch) {
        var vm = this;
        
        vm.formCells = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FormCell.query(function(result) {
                vm.formCells = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormCellSearch.query({query: vm.searchQuery}, function(result) {
                vm.formCells = result;
            });
        }    }
})();
