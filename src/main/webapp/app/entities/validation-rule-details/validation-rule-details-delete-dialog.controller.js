(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDetailsDeleteController',ValidationRuleDetailsDeleteController);

    ValidationRuleDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ValidationRuleDetails'];

    function ValidationRuleDetailsDeleteController($uibModalInstance, entity, ValidationRuleDetails) {
        var vm = this;

        vm.validationRuleDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ValidationRuleDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
