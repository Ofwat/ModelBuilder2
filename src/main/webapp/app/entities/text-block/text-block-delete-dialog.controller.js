(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextBlockDeleteController',TextBlockDeleteController);

    TextBlockDeleteController.$inject = ['$uibModalInstance', 'entity', 'TextBlock'];

    function TextBlockDeleteController($uibModalInstance, entity, TextBlock) {
        var vm = this;

        vm.textBlock = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TextBlock.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
