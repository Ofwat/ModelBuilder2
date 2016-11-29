(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextBlockController', TextBlockController);

    TextBlockController.$inject = ['$scope', '$state', 'TextBlock', 'TextBlockSearch'];

    function TextBlockController ($scope, $state, TextBlock, TextBlockSearch) {
        var vm = this;
        
        vm.textBlocks = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TextBlock.query(function(result) {
                vm.textBlocks = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TextBlockSearch.query({query: vm.searchQuery}, function(result) {
                vm.textBlocks = result;
            });
        }    }
})();
