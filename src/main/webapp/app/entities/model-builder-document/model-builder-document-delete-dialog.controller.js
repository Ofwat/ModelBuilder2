(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelBuilderDocumentDeleteController',ModelBuilderDocumentDeleteController);

    ModelBuilderDocumentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelBuilderDocument'];

    function ModelBuilderDocumentDeleteController($uibModalInstance, entity, ModelBuilderDocument) {
        var vm = this;

        vm.modelBuilderDocument = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelBuilderDocument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
