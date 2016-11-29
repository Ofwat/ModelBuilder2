(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDetailsDialogController', LineDetailsDialogController);

    LineDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LineDetails'];

    function LineDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LineDetails) {
        var vm = this;

        vm.lineDetails = entity;
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
            if (vm.lineDetails.id !== null) {
                LineDetails.update(vm.lineDetails, onSaveSuccess, onSaveError);
            } else {
                LineDetails.save(vm.lineDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:lineDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
