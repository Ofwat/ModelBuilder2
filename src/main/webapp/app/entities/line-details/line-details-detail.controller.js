(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDetailsDetailController', LineDetailsDetailController);

    LineDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LineDetails'];

    function LineDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, LineDetails) {
        var vm = this;

        vm.lineDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:lineDetailsUpdate', function(event, result) {
            vm.lineDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
