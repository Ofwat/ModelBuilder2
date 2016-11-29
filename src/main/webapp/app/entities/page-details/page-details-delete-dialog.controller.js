(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDetailsDeleteController',PageDetailsDeleteController);

    PageDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'PageDetails'];

    function PageDetailsDeleteController($uibModalInstance, entity, PageDetails) {
        var vm = this;

        vm.pageDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PageDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
