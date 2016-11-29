(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDetailsDialogController', ModelDetailsDialogController);

    ModelDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModelDetails'];

    function ModelDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModelDetails) {
        var vm = this;

        vm.modelDetails = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modelDetails.id !== null) {
                ModelDetails.update(vm.modelDetails, onSaveSuccess, onSaveError);
            } else {
                ModelDetails.save(vm.modelDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:modelDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastModified = false;
        vm.datePickerOpenStatus.created = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
