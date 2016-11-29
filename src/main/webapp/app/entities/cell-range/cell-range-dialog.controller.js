(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellRangeDialogController', CellRangeDialogController);

    CellRangeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CellRange'];

    function CellRangeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CellRange) {
        var vm = this;

        vm.cellRange = entity;
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
            if (vm.cellRange.id !== null) {
                CellRange.update(vm.cellRange, onSaveSuccess, onSaveError);
            } else {
                CellRange.save(vm.cellRange, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:cellRangeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
