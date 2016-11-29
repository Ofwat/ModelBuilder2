(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDetailController', ValidationRuleDetailController);

    ValidationRuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ValidationRule', 'ValidationRuleDetails', 'ValidationRuleItem', 'Model'];

    function ValidationRuleDetailController($scope, $rootScope, $stateParams, previousState, entity, ValidationRule, ValidationRuleDetails, ValidationRuleItem, Model) {
        var vm = this;

        vm.validationRule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:validationRuleUpdate', function(event, result) {
            vm.validationRule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
