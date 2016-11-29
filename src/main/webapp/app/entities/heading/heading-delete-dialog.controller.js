(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('HeadingDeleteController',HeadingDeleteController);

    HeadingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Heading'];

    function HeadingDeleteController($uibModalInstance, entity, Heading) {
        var vm = this;

        vm.heading = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Heading.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
