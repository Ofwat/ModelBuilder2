(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelBuilderDocumentDialogController', ModelBuilderDocumentDialogController);

    ModelBuilderDocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModelBuilderDocument', 'Model', 'Page', 'Section', 'Line'];

    function ModelBuilderDocumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModelBuilderDocument, Model, Page, Section, Line) {
        var vm = this;

        vm.modelBuilderDocument = entity;
        vm.clear = clear;
        vm.save = save;
        vm.models = Model.query();
        vm.pages = Page.query();
        vm.sections = Section.query();
        vm.lines = Line.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modelBuilderDocument.id !== null) {
                ModelBuilderDocument.update(vm.modelBuilderDocument, onSaveSuccess, onSaveError);
            } else {
                ModelBuilderDocument.save(vm.modelBuilderDocument, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:modelBuilderDocumentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
