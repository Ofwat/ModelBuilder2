(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearController', YearController);

    YearController.$inject = ['$scope', '$state', 'Year', 'YearSearch'];

    function YearController ($scope, $state, Year, YearSearch) {
        var vm = this;
        
        vm.years = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Year.query(function(result) {
                vm.years = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            YearSearch.query({query: vm.searchQuery}, function(result) {
                vm.years = result;
            });
        }    }
})();
