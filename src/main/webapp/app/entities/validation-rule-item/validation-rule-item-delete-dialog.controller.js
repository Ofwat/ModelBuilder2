(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleItemDeleteController',ValidationRuleItemDeleteController);

    ValidationRuleItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ValidationRuleItem'];

    function ValidationRuleItemDeleteController($uibModalInstance, entity, ValidationRuleItem) {
        var vm = this;

        vm.validationRuleItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ValidationRuleItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
