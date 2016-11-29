(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDetailsDeleteController',SectionDetailsDeleteController);

    SectionDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'SectionDetails'];

    function SectionDetailsDeleteController($uibModalInstance, entity, SectionDetails) {
        var vm = this;

        vm.sectionDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SectionDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
