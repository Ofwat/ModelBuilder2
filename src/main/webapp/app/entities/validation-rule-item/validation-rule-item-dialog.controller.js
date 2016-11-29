(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleItemDialogController', ValidationRuleItemDialogController);

    ValidationRuleItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ValidationRuleItem', 'ValidationRule'];

    function ValidationRuleItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ValidationRuleItem, ValidationRule) {
        var vm = this;

        vm.validationRuleItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.validationrules = ValidationRule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.validationRuleItem.id !== null) {
                ValidationRuleItem.update(vm.validationRuleItem, onSaveSuccess, onSaveError);
            } else {
                ValidationRuleItem.save(vm.validationRuleItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:validationRuleItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
