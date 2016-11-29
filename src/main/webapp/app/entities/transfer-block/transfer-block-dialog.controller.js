(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TransferBlockDialogController', TransferBlockDialogController);

    TransferBlockDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'TransferBlock', 'TransferBlockDetails', 'TransferBlockItem', 'Transfer'];

    function TransferBlockDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, TransferBlock, TransferBlockDetails, TransferBlockItem, Transfer) {
        var vm = this;

        vm.transferBlock = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transferblockdetails = TransferBlockDetails.query({filter: 'transferblock-is-null'});
        $q.all([vm.transferBlock.$promise, vm.transferblockdetails.$promise]).then(function() {
            if (!vm.transferBlock.transferBlockDetails || !vm.transferBlock.transferBlockDetails.id) {
                return $q.reject();
            }
            return TransferBlockDetails.get({id : vm.transferBlock.transferBlockDetails.id}).$promise;
        }).then(function(transferBlockDetails) {
            vm.transferblockdetails.push(transferBlockDetails);
        });
        vm.transferblockitems = TransferBlockItem.query();
        vm.transfers = Transfer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transferBlock.id !== null) {
                TransferBlock.update(vm.transferBlock, onSaveSuccess, onSaveError);
            } else {
                TransferBlock.save(vm.transferBlock, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:transferBlockUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
