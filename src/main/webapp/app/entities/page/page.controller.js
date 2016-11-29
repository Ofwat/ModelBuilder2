(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageController', PageController);

    PageController.$inject = ['$scope', '$state', 'Page', 'PageSearch'];

    function PageController ($scope, $state, Page, PageSearch) {
        var vm = this;
        
        vm.pages = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Page.query(function(result) {
                vm.pages = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PageSearch.query({query: vm.searchQuery}, function(result) {
                vm.pages = result;
            });
        }    }
})();
