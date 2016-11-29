(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDetailsDialogController', ValidationRuleDetailsDialogController);

    ValidationRuleDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ValidationRuleDetails'];

    function ValidationRuleDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ValidationRuleDetails) {
        var vm = this;

        vm.validationRuleDetails = entity;
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
            if (vm.validationRuleDetails.id !== null) {
                ValidationRuleDetails.update(vm.validationRuleDetails, onSaveSuccess, onSaveError);
            } else {
                ValidationRuleDetails.save(vm.validationRuleDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:validationRuleDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
