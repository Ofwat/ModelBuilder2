(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditDetailsDeleteController',AuditDetailsDeleteController);

    AuditDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuditDetails'];

    function AuditDetailsDeleteController($uibModalInstance, entity, AuditDetails) {
        var vm = this;

        vm.auditDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuditDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
