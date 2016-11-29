(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleItemDetailController', ValidationRuleItemDetailController);

    ValidationRuleItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ValidationRuleItem', 'ValidationRule'];

    function ValidationRuleItemDetailController($scope, $rootScope, $stateParams, previousState, entity, ValidationRuleItem, ValidationRule) {
        var vm = this;

        vm.validationRuleItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:validationRuleItemUpdate', function(event, result) {
            vm.validationRuleItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
