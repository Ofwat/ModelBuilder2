(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelAuditDeleteController',ModelAuditDeleteController);

    ModelAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelAudit'];

    function ModelAuditDeleteController($uibModalInstance, entity, ModelAudit) {
        var vm = this;

        vm.modelAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
