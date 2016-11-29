(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleItemController', ValidationRuleItemController);

    ValidationRuleItemController.$inject = ['$scope', '$state', 'ValidationRuleItem', 'ValidationRuleItemSearch'];

    function ValidationRuleItemController ($scope, $state, ValidationRuleItem, ValidationRuleItemSearch) {
        var vm = this;
        
        vm.validationRuleItems = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ValidationRuleItem.query(function(result) {
                vm.validationRuleItems = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ValidationRuleItemSearch.query({query: vm.searchQuery}, function(result) {
                vm.validationRuleItems = result;
            });
        }    }
})();
