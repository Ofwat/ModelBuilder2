(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextDeleteController',TextDeleteController);

    TextDeleteController.$inject = ['$uibModalInstance', 'entity', 'Text'];

    function TextDeleteController($uibModalInstance, entity, Text) {
        var vm = this;

        vm.text = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Text.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
