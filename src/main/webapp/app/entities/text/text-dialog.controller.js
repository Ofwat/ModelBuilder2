(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextDialogController', TextDialogController);

    TextDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Text', 'TextBlock', 'Model'];

    function TextDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Text, TextBlock, Model) {
        var vm = this;

        vm.text = entity;
        vm.clear = clear;
        vm.save = save;
        vm.textblocks = TextBlock.query();
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.text.id !== null) {
                Text.update(vm.text, onSaveSuccess, onSaveError);
            } else {
                Text.save(vm.text, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:textUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
