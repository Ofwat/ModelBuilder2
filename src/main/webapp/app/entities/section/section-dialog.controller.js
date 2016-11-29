(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDialogController', SectionDialogController);

    SectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Section', 'SectionDetails', 'ModelBuilderDocument', 'Line', 'Form', 'Page'];

    function SectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Section, SectionDetails, ModelBuilderDocument, Line, Form, Page) {
        var vm = this;

        vm.section = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sectiondetails = SectionDetails.query({filter: 'section-is-null'});
        $q.all([vm.section.$promise, vm.sectiondetails.$promise]).then(function() {
            if (!vm.section.sectionDetail || !vm.section.sectionDetail.id) {
                return $q.reject();
            }
            return SectionDetails.get({id : vm.section.sectionDetail.id}).$promise;
        }).then(function(sectionDetail) {
            vm.sectiondetails.push(sectionDetail);
        });
        vm.modelbuilderdocuments = ModelBuilderDocument.query();
        vm.lines = Line.query();
        vm.forms = Form.query();
        vm.pages = Page.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.section.id !== null) {
                Section.update(vm.section, onSaveSuccess, onSaveError);
            } else {
                Section.save(vm.section, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:sectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
