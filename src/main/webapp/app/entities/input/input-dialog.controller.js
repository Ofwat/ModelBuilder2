(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('InputDialogController', InputDialogController);

    InputDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Input', 'Model'];

    function InputDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Input, Model) {
        var vm = this;

        vm.input = entity;
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
            if (vm.input.id !== null) {
                Input.update(vm.input, onSaveSuccess, onSaveError);
            } else {
                Input.save(vm.input, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:inputUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
