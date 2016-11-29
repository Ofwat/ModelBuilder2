(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelAuditDialogController', ModelAuditDialogController);

    ModelAuditDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ModelAudit', 'AuditDetails', 'AuditChange', 'Model'];

    function ModelAuditDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ModelAudit, AuditDetails, AuditChange, Model) {
        var vm = this;

        vm.modelAudit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.modelauditdetails = AuditDetails.query({filter: 'modelaudit-is-null'});
        $q.all([vm.modelAudit.$promise, vm.modelauditdetails.$promise]).then(function() {
            if (!vm.modelAudit.modelAuditDetail || !vm.modelAudit.modelAuditDetail.id) {
                return $q.reject();
            }
            return AuditDetails.get({id : vm.modelAudit.modelAuditDetail.id}).$promise;
        }).then(function(modelAuditDetail) {
            vm.modelauditdetails.push(modelAuditDetail);
        });
        vm.auditchanges = AuditChange.query();
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modelAudit.id !== null) {
                ModelAudit.update(vm.modelAudit, onSaveSuccess, onSaveError);
            } else {
                ModelAudit.save(vm.modelAudit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:modelAuditUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
