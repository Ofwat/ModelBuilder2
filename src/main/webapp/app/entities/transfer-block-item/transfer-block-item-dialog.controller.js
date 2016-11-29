(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockItemDialogController', TransferBlockItemDialogController);

    TransferBlockItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransferBlockItem', 'YearCode', 'TransferBlock'];

    function TransferBlockItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransferBlockItem, YearCode, TransferBlock) {
        var vm = this;

        vm.transferBlockItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.yearcodes = YearCode.query();
        vm.transferblocks = TransferBlock.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transferBlockItem.id !== null) {
                TransferBlockItem.update(vm.transferBlockItem, onSaveSuccess, onSaveError);
            } else {
                TransferBlockItem.save(vm.transferBlockItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:transferBlockItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
