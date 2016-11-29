(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDeleteController',ModelDeleteController);

    ModelDeleteController.$inject = ['$uibModalInstance', 'entity', 'Model'];

    function ModelDeleteController($uibModalInstance, entity, Model) {
        var vm = this;

        vm.model = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Model.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
