(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelController', ModelController);

    ModelController.$inject = ['$scope', '$state', 'Model', 'ModelSearch'];

    function ModelController ($scope, $state, Model, ModelSearch) {
        var vm = this;
        
        vm.models = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Model.query(function(result) {
                vm.models = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelSearch.query({query: vm.searchQuery}, function(result) {
                vm.models = result;
            });
        }    }
})();
