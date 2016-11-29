(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDialogController', ValidationRuleDialogController);

    ValidationRuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ValidationRule', 'ValidationRuleDetails', 'ValidationRuleItem', 'Model'];

    function ValidationRuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ValidationRule, ValidationRuleDetails, ValidationRuleItem, Model) {
        var vm = this;

        vm.validationRule = entity;
        vm.clear = clear;
        vm.save = save;
        vm.validationruledetails = ValidationRuleDetails.query({filter: 'validationrule-is-null'});
        $q.all([vm.validationRule.$promise, vm.validationruledetails.$promise]).then(function() {
            if (!vm.validationRule.validationRuleDetail || !vm.validationRule.validationRuleDetail.id) {
                return $q.reject();
            }
            return ValidationRuleDetails.get({id : vm.validationRule.validationRuleDetail.id}).$promise;
        }).then(function(validationRuleDetail) {
            vm.validationruledetails.push(validationRuleDetail);
        });
        vm.validationruleitems = ValidationRuleItem.query();
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.validationRule.id !== null) {
                ValidationRule.update(vm.validationRule, onSaveSuccess, onSaveError);
            } else {
                ValidationRule.save(vm.validationRule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:validationRuleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
