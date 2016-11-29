'use strict';

angular.module('modelBuilderApp').controller('CopyModelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance','Model', 'ModelBuilder', 'entity',
        function($scope, $stateParams, $uibModalInstance, Model, ModelBuilder, entity) {

            //Add the entity to the scope - this will automatically turn the promise to am actual solid entity.
            $scope.modelDetails = entity;
            $scope.newName = 'Copy of ' + entity.name;
            $scope.newCode = 'Copy of ' + entity.code;
            $scope.clear = function() {
                $uibModalInstance.dismiss('cancel');
            };


            $scope.load = function() {
                console.log("Loading controller")
            };


            var onSaveSuccess = function (result) {
                $scope.$emit('modelBuilderApp:copiedModel', result);
                $uibModalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                console.log("There was an error " + result);
                $scope.isSaving = false;
            };

            $scope.save = function () {
                console.log("Copying a model");
                $scope.isSaving = true;
                ModelBuilder.copy({id:$scope.modelDetails.id}, {name:$scope.newName, code:$scope.newCode}, onSaveSuccess, onSaveError)
            };
        }]);
