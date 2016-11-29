(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDetailsDeleteController',LineDetailsDeleteController);

    LineDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'LineDetails'];

    function LineDetailsDeleteController($uibModalInstance, entity, LineDetails) {
        var vm = this;

        vm.lineDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LineDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
