(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearCodeDeleteController',YearCodeDeleteController);

    YearCodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'YearCode'];

    function YearCodeDeleteController($uibModalInstance, entity, YearCode) {
        var vm = this;

        vm.yearCode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            YearCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
