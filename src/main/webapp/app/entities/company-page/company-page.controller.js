(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CompanyPageController', CompanyPageController);

    CompanyPageController.$inject = ['$scope', '$state', 'CompanyPage', 'CompanyPageSearch'];

    function CompanyPageController ($scope, $state, CompanyPage, CompanyPageSearch) {
        var vm = this;
        
        vm.companyPages = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CompanyPage.query(function(result) {
                vm.companyPages = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanyPageSearch.query({query: vm.searchQuery}, function(result) {
                vm.companyPages = result;
            });
        }    }
})();
