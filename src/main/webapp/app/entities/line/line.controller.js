(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineController', LineController);

    LineController.$inject = ['$scope', '$state', 'Line', 'LineSearch'];

    function LineController ($scope, $state, Line, LineSearch) {
        var vm = this;
        
        vm.lines = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Line.query(function(result) {
                vm.lines = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LineSearch.query({query: vm.searchQuery}, function(result) {
                vm.lines = result;
            });
        }    }
})();
