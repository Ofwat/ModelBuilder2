(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditDetailsDialogController', AuditDetailsDialogController);

    AuditDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditDetails'];

    function AuditDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditDetails) {
        var vm = this;

        vm.auditDetails = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditDetails.id !== null) {
                AuditDetails.update(vm.auditDetails, onSaveSuccess, onSaveError);
            } else {
                AuditDetails.save(vm.auditDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:auditDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
