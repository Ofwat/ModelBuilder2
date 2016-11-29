(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDeleteController',ValidationRuleDeleteController);

    ValidationRuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'ValidationRule'];

    function ValidationRuleDeleteController($uibModalInstance, entity, ValidationRule) {
        var vm = this;

        vm.validationRule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ValidationRule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
