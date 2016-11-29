(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('InputDeleteController',InputDeleteController);

    InputDeleteController.$inject = ['$uibModalInstance', 'entity', 'Input'];

    function InputDeleteController($uibModalInstance, entity, Input) {
        var vm = this;

        vm.input = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Input.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
