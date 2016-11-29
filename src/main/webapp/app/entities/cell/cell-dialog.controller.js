(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('CellDialogController', CellDialogController);

    CellDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cell', 'Line'];

    function CellDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cell, Line) {
        var vm = this;

        vm.cell = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lines = Line.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cell.id !== null) {
                Cell.update(vm.cell, onSaveSuccess, onSaveError);
            } else {
                Cell.save(vm.cell, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:cellUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
