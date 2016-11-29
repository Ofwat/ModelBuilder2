(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('YearCodeDialogController', YearCodeDialogController);

    YearCodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'YearCode', 'TransferBlockItem'];

    function YearCodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, YearCode, TransferBlockItem) {
        var vm = this;

        vm.yearCode = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transferblockitems = TransferBlockItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.yearCode.id !== null) {
                YearCode.update(vm.yearCode, onSaveSuccess, onSaveError);
            } else {
                YearCode.save(vm.yearCode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:yearCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
