(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('HeadingDetailController', HeadingDetailController);

    HeadingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Heading', 'Model'];

    function HeadingDetailController($scope, $rootScope, $stateParams, previousState, entity, Heading, Model) {
        var vm = this;

        vm.heading = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:headingUpdate', function(event, result) {
            vm.heading = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
