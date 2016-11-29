(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDetailsController', LineDetailsController);

    LineDetailsController.$inject = ['$scope', '$state', 'LineDetails', 'LineDetailsSearch'];

    function LineDetailsController ($scope, $state, LineDetails, LineDetailsSearch) {
        var vm = this;
        
        vm.lineDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LineDetails.query(function(result) {
                vm.lineDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LineDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.lineDetails = result;
            });
        }    }
})();
