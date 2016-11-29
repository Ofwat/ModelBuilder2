(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormController', FormController);

    FormController.$inject = ['$scope', '$state', 'Form', 'FormSearch'];

    function FormController ($scope, $state, Form, FormSearch) {
        var vm = this;
        
        vm.forms = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Form.query(function(result) {
                vm.forms = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FormSearch.query({query: vm.searchQuery}, function(result) {
                vm.forms = result;
            });
        }    }
})();
