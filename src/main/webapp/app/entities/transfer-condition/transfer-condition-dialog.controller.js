(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferConditionDialogController', TransferConditionDialogController);

    TransferConditionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransferCondition'];

    function TransferConditionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransferCondition) {
        var vm = this;

        vm.transferCondition = entity;
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
            if (vm.transferCondition.id !== null) {
                TransferCondition.update(vm.transferCondition, onSaveSuccess, onSaveError);
            } else {
                TransferCondition.save(vm.transferCondition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:transferConditionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
