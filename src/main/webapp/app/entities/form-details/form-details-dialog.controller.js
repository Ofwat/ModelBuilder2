(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDetailsDialogController', FormDetailsDialogController);

    FormDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormDetails'];

    function FormDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormDetails) {
        var vm = this;

        vm.formDetails = entity;
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
            if (vm.formDetails.id !== null) {
                FormDetails.update(vm.formDetails, onSaveSuccess, onSaveError);
            } else {
                FormDetails.save(vm.formDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:formDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
