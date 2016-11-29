(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('MacroDeleteController',MacroDeleteController);

    MacroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Macro'];

    function MacroDeleteController($uibModalInstance, entity, Macro) {
        var vm = this;

        vm.macro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Macro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
