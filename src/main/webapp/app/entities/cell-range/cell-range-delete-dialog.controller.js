(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellRangeDeleteController',CellRangeDeleteController);

    CellRangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CellRange'];

    function CellRangeDeleteController($uibModalInstance, entity, CellRange) {
        var vm = this;

        vm.cellRange = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CellRange.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
