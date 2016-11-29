(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormHeadingCellController', FormHeadingCellController);

    FormHeadingCellController.$inject = ['$scope', '$state', 'FormHeadingCell', 'FormHeadingCellSearch'];

    function FormHeadingCellController ($scope, $state, FormHeadingCell, FormHeadingCellSearch) {
        var vm = this;
        
        vm.formHeadingCells = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FormHeadingCell.query(function(result) {
                vm.formHeadingCells = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormHeadingCellSearch.query({query: vm.searchQuery}, function(result) {
                vm.formHeadingCells = result;
            });
        }    }
})();
