(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditDetailsDetailController', AuditDetailsDetailController);

    AuditDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditDetails'];

    function AuditDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditDetails) {
        var vm = this;

        vm.auditDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:auditDetailsUpdate', function(event, result) {
            vm.auditDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
