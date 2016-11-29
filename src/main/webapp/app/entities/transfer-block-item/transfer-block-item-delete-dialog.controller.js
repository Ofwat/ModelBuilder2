(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockItemDeleteController',TransferBlockItemDeleteController);

    TransferBlockItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransferBlockItem'];

    function TransferBlockItemDeleteController($uibModalInstance, entity, TransferBlockItem) {
        var vm = this;

        vm.transferBlockItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransferBlockItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
