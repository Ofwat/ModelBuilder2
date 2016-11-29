(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('HeadingController', HeadingController);

    HeadingController.$inject = ['$scope', '$state', 'Heading', 'HeadingSearch'];

    function HeadingController ($scope, $state, Heading, HeadingSearch) {
        var vm = this;
        
        vm.headings = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Heading.query(function(result) {
                vm.headings = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HeadingSearch.query({query: vm.searchQuery}, function(result) {
                vm.headings = result;
            });
        }    }
})();
