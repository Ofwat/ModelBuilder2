(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferConditionDeleteController',TransferConditionDeleteController);

    TransferConditionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransferCondition'];

    function TransferConditionDeleteController($uibModalInstance, entity, TransferCondition) {
        var vm = this;

        vm.transferCondition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransferCondition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
