(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('InputController', InputController);

    InputController.$inject = ['$scope', '$state', 'Input', 'InputSearch'];

    function InputController ($scope, $state, Input, InputSearch) {
        var vm = this;
        
        vm.inputs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Input.query(function(result) {
                vm.inputs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InputSearch.query({query: vm.searchQuery}, function(result) {
                vm.inputs = result;
            });
        }    }
})();
