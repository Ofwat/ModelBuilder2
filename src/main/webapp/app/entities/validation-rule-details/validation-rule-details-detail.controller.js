(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ValidationRuleDetailsDetailController', ValidationRuleDetailsDetailController);

    ValidationRuleDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ValidationRuleDetails'];

    function ValidationRuleDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, ValidationRuleDetails) {
        var vm = this;

        vm.validationRuleDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:validationRuleDetailsUpdate', function(event, result) {
            vm.validationRuleDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
