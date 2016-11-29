(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('AuditChangeDetailController', AuditChangeDetailController);

    AuditChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditChange', 'ModelAudit'];

    function AuditChangeDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditChange, ModelAudit) {
        var vm = this;

        vm.auditChange = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:auditChangeUpdate', function(event, result) {
            vm.auditChange = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
