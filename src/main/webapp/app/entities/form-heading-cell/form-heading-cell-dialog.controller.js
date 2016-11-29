(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormHeadingCellDialogController', FormHeadingCellDialogController);

    FormHeadingCellDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormHeadingCell', 'Form'];

    function FormHeadingCellDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormHeadingCell, Form) {
        var vm = this;

        vm.formHeadingCell = entity;
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
            if (vm.formHeadingCell.id !== null) {
                FormHeadingCell.update(vm.formHeadingCell, onSaveSuccess, onSaveError);
            } else {
                FormHeadingCell.save(vm.formHeadingCell, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:formHeadingCellUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
