(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleController', ValidationRuleController);

    ValidationRuleController.$inject = ['$scope', '$state', 'ValidationRule', 'ValidationRuleSearch'];

    function ValidationRuleController ($scope, $state, ValidationRule, ValidationRuleSearch) {
        var vm = this;
        
        vm.validationRules = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ValidationRule.query(function(result) {
                vm.validationRules = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ValidationRuleSearch.query({query: vm.searchQuery}, function(result) {
                vm.validationRules = result;
            });
        }    }
})();
