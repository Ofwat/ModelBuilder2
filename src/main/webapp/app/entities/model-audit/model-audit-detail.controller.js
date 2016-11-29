(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelAuditDetailController', ModelAuditDetailController);

    ModelAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelAudit', 'AuditDetails', 'AuditChange', 'Model'];

    function ModelAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelAudit, AuditDetails, AuditChange, Model) {
        var vm = this;

        vm.modelAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('modelBuilderApp:modelAuditUpdate', function(event, result) {
            vm.modelAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
