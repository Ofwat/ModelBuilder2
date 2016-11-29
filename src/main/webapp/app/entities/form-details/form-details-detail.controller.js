(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('FormDetailsDetailController', FormDetailsDetailController);

    FormDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormDetails'];

    function FormDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, FormDetails) {
        var vm = this;

        vm.formDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:formDetailsUpdate', function(event, result) {
            vm.formDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
