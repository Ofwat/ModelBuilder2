(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellDeleteController',CellDeleteController);

    CellDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cell'];

    function CellDeleteController($uibModalInstance, entity, Cell) {
        var vm = this;

        vm.cell = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cell.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
