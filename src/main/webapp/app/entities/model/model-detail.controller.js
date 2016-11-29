(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelDetailController', ModelDetailController);

    ModelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Model', 'ModelDetails', 'ModelAudit', 'Item', 'Year', 'Input', 'Heading', 'ValidationRule', 'CompanyPage', 'ModelBuilderDocument', 'Page', 'Transfer', 'Macro', 'Text'];

    function ModelDetailController($scope, $rootScope, $stateParams, previousState, entity, Model, ModelDetails, ModelAudit, Item, Year, Input, Heading, ValidationRule, CompanyPage, ModelBuilderDocument, Page, Transfer, Macro, Text) {
        var vm = this;

        vm.model = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:modelUpdate', function(event, result) {
            vm.model = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
