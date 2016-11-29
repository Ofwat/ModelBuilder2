(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelAuditController', ModelAuditController);

    ModelAuditController.$inject = ['$scope', '$state', 'ModelAudit', 'ModelAuditSearch'];

    function ModelAuditController ($scope, $state, ModelAudit, ModelAuditSearch) {
        var vm = this;
        
        vm.modelAudits = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelAudit.query(function(result) {
                vm.modelAudits = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelAuditSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelAudits = result;
            });
        }    }
})();
