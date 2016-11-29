(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CompanyPageDialogController', CompanyPageDialogController);

    CompanyPageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyPage', 'Model'];

    function CompanyPageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyPage, Model) {
        var vm = this;

        vm.companyPage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyPage.id !== null) {
                CompanyPage.update(vm.companyPage, onSaveSuccess, onSaveError);
            } else {
                CompanyPage.save(vm.companyPage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:companyPageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
