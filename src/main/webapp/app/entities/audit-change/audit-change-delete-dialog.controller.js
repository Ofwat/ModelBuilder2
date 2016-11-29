(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditChangeDeleteController',AuditChangeDeleteController);

    AuditChangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuditChange'];

    function AuditChangeDeleteController($uibModalInstance, entity, AuditChange) {
        var vm = this;

        vm.auditChange = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuditChange.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
