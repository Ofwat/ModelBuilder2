(function(){

    'use strict';

    angular.module('modelBuilderApp')
        .controller('ModelBuilderController', ModelBuilderController);

    ModelBuilderController.$inject = ['$scope', '$state', '$location', 'Model', 'ModelBuilder', 'ModelExporter', 'ModelSearch', 'fountainModelService', 'entity'];

    function ModelBuilderController($scope, $state, $location, Model, ModelBuilder, ModelExporter, ModelSearch, fountainModelService, entity) {

        $scope.models = [];
        $scope.model = entity;

        if (($scope.model.modelDetails != null) && ($scope.model.modelDetails.displayOrder == null)) {
            $scope.model.modelDetails.displayOrder = 1;
        }


        $scope.families = fountainModelService.getModelFamilies();
        $scope.runs = fountainModelService.getRuns();
        $scope.modelTypes = fountainModelService.getModelTypes();


        //TODO - Remove this function.
        $scope.loadAll = function () {
        };


        $scope.sendToFountain = function (entity) {
            $scope.isSendingToFountain = true;
            console.log("Sending the model to Fountain");
            ModelExporter.export({id: entity.id}, entity, onSendToFountainSuccess, onSendToFountainError);
        };

        var onSendToFountainSuccess = function (result) {
            console.log(result);
            console.log("Send To Fountain success");
            $scope.$emit('modelBuilderApp:sendToFountainUpdate', result); //Nothing subscribes to this yet.
            $scope.isSendingToFountain = false;
        };

        var onSendToFountainError = function (result) {
            console.log(result);
            console.log("Send To Fountain Error");
            $scope.$emit('modelBuilderApp:sendToFountainError', result);
            $scope.isSendingToFountain = false;
        };

        var onSaveSuccess = function (result) {
            console.log(result);
            console.log("Save success");
            $scope.$emit('modelBuilderApp:modelDetailsUpdate', result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            console.log(result);
            console.log("Save Error");
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            ModelBuilder.update($scope.model.modelDetails, onSaveSuccess, onSaveError);
            console.log("Saving a modelBuilder");
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            console.log("Clearing Scope");
            $scope.model = null;
            $location.path('/models'); // path not hash
        };
    }
})();
