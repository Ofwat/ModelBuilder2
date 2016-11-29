(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('MacroDialogController', MacroDialogController);

    MacroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Macro', 'Model'];

    function MacroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Macro, Model) {
        var vm = this;

        vm.macro = entity;
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
            if (vm.macro.id !== null) {
                Macro.update(vm.macro, onSaveSuccess, onSaveError);
            } else {
                Macro.save(vm.macro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:macroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
