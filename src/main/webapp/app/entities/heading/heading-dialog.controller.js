(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('HeadingDialogController', HeadingDialogController);

    HeadingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Heading', 'Model'];

    function HeadingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Heading, Model) {
        var vm = this;

        vm.heading = entity;
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
            if (vm.heading.id !== null) {
                Heading.update(vm.heading, onSaveSuccess, onSaveError);
            } else {
                Heading.save(vm.heading, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:headingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
