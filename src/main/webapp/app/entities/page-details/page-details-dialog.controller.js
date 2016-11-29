(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDetailsDialogController', PageDetailsDialogController);

    PageDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PageDetails'];

    function PageDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PageDetails) {
        var vm = this;

        vm.pageDetails = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pageDetails.id !== null) {
                PageDetails.update(vm.pageDetails, onSaveSuccess, onSaveError);
            } else {
                PageDetails.save(vm.pageDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:pageDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
