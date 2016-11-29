(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDialogController', FormDialogController);

    FormDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Form', 'FormDetails', 'FormCell', 'FormHeadingCell', 'Section'];

    function FormDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Form, FormDetails, FormCell, FormHeadingCell, Section) {
        var vm = this;

        vm.form = entity;
        vm.clear = clear;
        vm.save = save;
        vm.formdetails = FormDetails.query({filter: 'form-is-null'});
        $q.all([vm.form.$promise, vm.formdetails.$promise]).then(function() {
            if (!vm.form.formDetail || !vm.form.formDetail.id) {
                return $q.reject();
            }
            return FormDetails.get({id : vm.form.formDetail.id}).$promise;
        }).then(function(formDetail) {
            vm.formdetails.push(formDetail);
        });
        vm.formcells = FormCell.query();
        vm.formheadingcells = FormHeadingCell.query();
        vm.sections = Section.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.form.id !== null) {
                Form.update(vm.form, onSaveSuccess, onSaveError);
            } else {
                Form.save(vm.form, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:formUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
