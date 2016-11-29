(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormCellDialogController', FormCellDialogController);

    FormCellDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormCell', 'Form'];

    function FormCellDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormCell, Form) {
        var vm = this;

        vm.formCell = entity;
        vm.clear = clear;
        vm.save = save;
        vm.forms = Form.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.formCell.id !== null) {
                FormCell.update(vm.formCell, onSaveSuccess, onSaveError);
            } else {
                FormCell.save(vm.formCell, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:formCellUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
