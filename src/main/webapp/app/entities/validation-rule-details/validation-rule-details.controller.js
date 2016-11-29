(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDetailsController', ValidationRuleDetailsController);

    ValidationRuleDetailsController.$inject = ['$scope', '$state', 'ValidationRuleDetails', 'ValidationRuleDetailsSearch'];

    function ValidationRuleDetailsController ($scope, $state, ValidationRuleDetails, ValidationRuleDetailsSearch) {
        var vm = this;
        
        vm.validationRuleDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ValidationRuleDetails.query(function(result) {
                vm.validationRuleDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ValidationRuleDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.validationRuleDetails = result;
            });
        }    }
})();
