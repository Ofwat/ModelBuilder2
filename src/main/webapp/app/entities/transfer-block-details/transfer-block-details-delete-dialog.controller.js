(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDetailsDeleteController',TransferBlockDetailsDeleteController);

    TransferBlockDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransferBlockDetails'];

    function TransferBlockDetailsDeleteController($uibModalInstance, entity, TransferBlockDetails) {
        var vm = this;

        vm.transferBlockDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransferBlockDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
