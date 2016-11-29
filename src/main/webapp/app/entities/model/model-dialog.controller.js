(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDialogController', ModelDialogController);

    ModelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Model', 'ModelDetails', 'ModelAudit', 'Item', 'Year', 'Input', 'Heading', 'ValidationRule', 'CompanyPage', 'ModelBuilderDocument', 'Page', 'Transfer', 'Macro', 'Text'];

    function ModelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Model, ModelDetails, ModelAudit, Item, Year, Input, Heading, ValidationRule, CompanyPage, ModelBuilderDocument, Page, Transfer, Macro, Text) {
        var vm = this;

        vm.model = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modeldetails = ModelDetails.query({filter: 'model-is-null'});
        $q.all([vm.model.$promise, vm.modeldetails.$promise]).then(function() {
            if (!vm.model.modelDetails || !vm.model.modelDetails.id) {
                return $q.reject();
            }
            return ModelDetails.get({id : vm.model.modelDetails.id}).$promise;
        }).then(function(modelDetails) {
            vm.modeldetails.push(modelDetails);
        });
        vm.modelaudits = ModelAudit.query();
        vm.items = Item.query();
        vm.years = Year.query();
        vm.inputs = Input.query();
        vm.headings = Heading.query();
        vm.validationrules = ValidationRule.query();
        vm.companypages = CompanyPage.query();
        vm.modelbuilderdocuments = ModelBuilderDocument.query();
        vm.pages = Page.query();
        vm.transfers = Transfer.query();
        vm.macros = Macro.query();
        vm.texts = Text.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.model.id !== null) {
                Model.update(vm.model, onSaveSuccess, onSaveError);
            } else {
                Model.save(vm.model, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:modelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
