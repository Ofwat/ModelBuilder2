(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditChangeDialogController', AuditChangeDialogController);

    AuditChangeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditChange', 'ModelAudit'];

    function AuditChangeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditChange, ModelAudit) {
        var vm = this;

        vm.auditChange = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modelaudits = ModelAudit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditChange.id !== null) {
                AuditChange.update(vm.auditChange, onSaveSuccess, onSaveError);
            } else {
                AuditChange.save(vm.auditChange, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:auditChangeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
