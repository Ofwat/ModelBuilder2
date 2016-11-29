(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearCodeController', YearCodeController);

    YearCodeController.$inject = ['$scope', '$state', 'YearCode', 'YearCodeSearch'];

    function YearCodeController ($scope, $state, YearCode, YearCodeSearch) {
        var vm = this;
        
        vm.yearCodes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            YearCode.query(function(result) {
                vm.yearCodes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            YearCodeSearch.query({query: vm.searchQuery}, function(result) {
                vm.yearCodes = result;
            });
        }    }
})();
