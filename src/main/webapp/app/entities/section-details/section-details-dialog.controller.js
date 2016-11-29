(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDetailsDialogController', SectionDetailsDialogController);

    SectionDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SectionDetails'];

    function SectionDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SectionDetails) {
        var vm = this;

        vm.sectionDetails = entity;
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
            if (vm.sectionDetails.id !== null) {
                SectionDetails.update(vm.sectionDetails, onSaveSuccess, onSaveError);
            } else {
                SectionDetails.save(vm.sectionDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:sectionDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
