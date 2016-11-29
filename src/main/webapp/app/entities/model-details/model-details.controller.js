(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDetailsController', ModelDetailsController);

    ModelDetailsController.$inject = ['$scope', '$state', 'ModelDetails', 'ModelDetailsSearch'];

    function ModelDetailsController ($scope, $state, ModelDetails, ModelDetailsSearch) {
        var vm = this;
        
        vm.modelDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelDetails.query(function(result) {
                vm.modelDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelDetails = result;
            });
        }    }
})();
