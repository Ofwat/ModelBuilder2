(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDetailsController', FormDetailsController);

    FormDetailsController.$inject = ['$scope', '$state', 'FormDetails', 'FormDetailsSearch'];

    function FormDetailsController ($scope, $state, FormDetails, FormDetailsSearch) {
        var vm = this;
        
        vm.formDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FormDetails.query(function(result) {
                vm.formDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.formDetails = result;
            });
        }    }
})();
