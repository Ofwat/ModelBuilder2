(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDetailsController', PageDetailsController);

    PageDetailsController.$inject = ['$scope', '$state', 'PageDetails', 'PageDetailsSearch'];

    function PageDetailsController ($scope, $state, PageDetails, PageDetailsSearch) {
        var vm = this;
        
        vm.pageDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PageDetails.query(function(result) {
                vm.pageDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PageDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.pageDetails = result;
            });
        }    }
})();
