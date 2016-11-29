(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('MacroController', MacroController);

    MacroController.$inject = ['$scope', '$state', 'Macro', 'MacroSearch'];

    function MacroController ($scope, $state, Macro, MacroSearch) {
        var vm = this;
        
        vm.macros = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Macro.query(function(result) {
                vm.macros = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MacroSearch.query({query: vm.searchQuery}, function(result) {
                vm.macros = result;
            });
        }    }
})();
