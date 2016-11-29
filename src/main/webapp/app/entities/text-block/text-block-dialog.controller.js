(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextBlockDialogController', TextBlockDialogController);

    TextBlockDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TextBlock', 'Text'];

    function TextBlockDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TextBlock, Text) {
        var vm = this;

        vm.textBlock = entity;
        vm.clear = clear;
        vm.save = save;
        vm.texts = Text.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.textBlock.id !== null) {
                TextBlock.update(vm.textBlock, onSaveSuccess, onSaveError);
            } else {
                TextBlock.save(vm.textBlock, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:textBlockUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
