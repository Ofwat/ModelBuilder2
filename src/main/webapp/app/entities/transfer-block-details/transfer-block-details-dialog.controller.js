(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDetailsDialogController', TransferBlockDetailsDialogController);

    TransferBlockDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransferBlockDetails'];

    function TransferBlockDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransferBlockDetails) {
        var vm = this;

        vm.transferBlockDetails = entity;
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
            if (vm.transferBlockDetails.id !== null) {
                TransferBlockDetails.update(vm.transferBlockDetails, onSaveSuccess, onSaveError);
            } else {
                TransferBlockDetails.save(vm.transferBlockDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:transferBlockDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
