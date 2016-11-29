(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CompanyPageDeleteController',CompanyPageDeleteController);

    CompanyPageDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyPage'];

    function CompanyPageDeleteController($uibModalInstance, entity, CompanyPage) {
        var vm = this;

        vm.companyPage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyPage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
