(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelBuilderDocumentDetailController', ModelBuilderDocumentDetailController);

    ModelBuilderDocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelBuilderDocument', 'Model', 'Page', 'Section', 'Line'];

    function ModelBuilderDocumentDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelBuilderDocument, Model, Page, Section, Line) {
        var vm = this;

        vm.modelBuilderDocument = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:modelBuilderDocumentUpdate', function(event, result) {
            vm.modelBuilderDocument = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
