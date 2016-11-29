(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditChangeController', AuditChangeController);

    AuditChangeController.$inject = ['$scope', '$state', 'AuditChange', 'AuditChangeSearch'];

    function AuditChangeController ($scope, $state, AuditChange, AuditChangeSearch) {
        var vm = this;
        
        vm.auditChanges = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AuditChange.query(function(result) {
                vm.auditChanges = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AuditChangeSearch.query({query: vm.searchQuery}, function(result) {
                vm.auditChanges = result;
            });
        }    }
})();
