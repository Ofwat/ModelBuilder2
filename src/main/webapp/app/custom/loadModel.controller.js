(function() {

    'use strict';

    angular.module('modelBuilderApp')
        .controller('LoadModelController', LoadModelController);

    LoadModelController.$inject = ['$scope', '$state', '$timeout', 'Model', 'ModelBuilder', 'ModelSearch', 'fountainModelService', 'Upload'];

    function LoadModelController($scope, $state, $timeout, Model, ModelBuilder, ModelSearch, fountainModelService, Upload) {
        console.log(Upload);
        $scope.uploadModel = function (file) {
            console.log(file);
            file.upload = Upload.upload({
                //TODO we need to get the properties in the Angular App
                url: 'api/models/upload',
                data: {file: file, name: file.name},
            });
            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                // Math.min is to fix IE which reports 200% sometimes
                file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            });
        }
    }
})()
