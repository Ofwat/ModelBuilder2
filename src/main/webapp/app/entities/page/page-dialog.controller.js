(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('PageDialogController', PageDialogController);

    PageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Page', 'PageDetails', 'Section', 'ModelBuilderDocument', 'Model'];

    function PageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Page, PageDetails, Section, ModelBuilderDocument, Model) {
        var vm = this;

        vm.page = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pagedetails = PageDetails.query({filter: 'page-is-null'});
        $q.all([vm.page.$promise, vm.pagedetails.$promise]).then(function() {
            if (!vm.page.pageDetail || !vm.page.pageDetail.id) {
                return $q.reject();
            }
            return PageDetails.get({id : vm.page.pageDetail.id}).$promise;
        }).then(function(pageDetail) {
            vm.pagedetails.push(pageDetail);
        });
        vm.sections = Section.query();
        vm.modelbuilderdocuments = ModelBuilderDocument.query();
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.page.id !== null) {
                Page.update(vm.page, onSaveSuccess, onSaveError);
            } else {
                Page.save(vm.page, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:pageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
