(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditDetailsController', AuditDetailsController);

    AuditDetailsController.$inject = ['$scope', '$state', 'AuditDetails', 'AuditDetailsSearch'];

    function AuditDetailsController ($scope, $state, AuditDetails, AuditDetailsSearch) {
        var vm = this;
        
        vm.auditDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AuditDetails.query(function(result) {
                vm.auditDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AuditDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.auditDetails = result;
            });
        }    }
})();
