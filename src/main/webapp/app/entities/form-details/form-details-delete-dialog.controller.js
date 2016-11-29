(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDetailsDeleteController',FormDetailsDeleteController);

    FormDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormDetails'];

    function FormDetailsDeleteController($uibModalInstance, entity, FormDetails) {
        var vm = this;

        vm.formDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
