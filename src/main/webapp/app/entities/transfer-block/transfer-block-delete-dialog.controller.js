(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDeleteController',TransferBlockDeleteController);

    TransferBlockDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransferBlock'];

    function TransferBlockDeleteController($uibModalInstance, entity, TransferBlock) {
        var vm = this;

        vm.transferBlock = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransferBlock.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
