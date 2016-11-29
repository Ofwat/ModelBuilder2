(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferDialogController', TransferDialogController);

    TransferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Transfer', 'TransferCondition', 'TransferBlock', 'Model'];

    function TransferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Transfer, TransferCondition, TransferBlock, Model) {
        var vm = this;

        vm.transfer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transferconditions = TransferCondition.query({filter: 'transfer-is-null'});
        $q.all([vm.transfer.$promise, vm.transferconditions.$promise]).then(function() {
            if (!vm.transfer.transferCondition || !vm.transfer.transferCondition.id) {
                return $q.reject();
            }
            return TransferCondition.get({id : vm.transfer.transferCondition.id}).$promise;
        }).then(function(transferCondition) {
            vm.transferconditions.push(transferCondition);
        });
        vm.transferblocks = TransferBlock.query();
        vm.models = Model.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transfer.id !== null) {
                Transfer.update(vm.transfer, onSaveSuccess, onSaveError);
            } else {
                Transfer.save(vm.transfer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:transferUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
