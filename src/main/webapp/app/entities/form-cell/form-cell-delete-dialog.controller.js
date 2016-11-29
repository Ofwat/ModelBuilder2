(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormCellDeleteController',FormCellDeleteController);

    FormCellDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormCell'];

    function FormCellDeleteController($uibModalInstance, entity, FormCell) {
        var vm = this;

        vm.formCell = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormCell.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
