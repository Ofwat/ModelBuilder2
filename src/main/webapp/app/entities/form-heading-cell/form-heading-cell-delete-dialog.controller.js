(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormHeadingCellDeleteController',FormHeadingCellDeleteController);

    FormHeadingCellDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormHeadingCell'];

    function FormHeadingCellDeleteController($uibModalInstance, entity, FormHeadingCell) {
        var vm = this;

        vm.formHeadingCell = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormHeadingCell.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
