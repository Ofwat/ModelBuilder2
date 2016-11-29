(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('LineDialogController', LineDialogController);

    LineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Line', 'LineDetails', 'CellRange', 'Cell', 'ModelBuilderDocument', 'Section'];

    function LineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Line, LineDetails, CellRange, Cell, ModelBuilderDocument, Section) {
        var vm = this;

        vm.line = entity;
        vm.clear = clear;
        vm.save = save;
        vm.linedetails = LineDetails.query({filter: 'line-is-null'});
        $q.all([vm.line.$promise, vm.linedetails.$promise]).then(function() {
            if (!vm.line.lineDetail || !vm.line.lineDetail.id) {
                return $q.reject();
            }
            return LineDetails.get({id : vm.line.lineDetail.id}).$promise;
        }).then(function(lineDetail) {
            vm.linedetails.push(lineDetail);
        });
        vm.cellranges = CellRange.query({filter: 'line-is-null'});
        $q.all([vm.line.$promise, vm.cellranges.$promise]).then(function() {
            if (!vm.line.cellRange || !vm.line.cellRange.id) {
                return $q.reject();
            }
            return CellRange.get({id : vm.line.cellRange.id}).$promise;
        }).then(function(cellRange) {
            vm.cellranges.push(cellRange);
        });
        vm.cells = Cell.query();
        vm.modelbuilderdocuments = ModelBuilderDocument.query();
        vm.sections = Section.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.line.id !== null) {
                Line.update(vm.line, onSaveSuccess, onSaveError);
            } else {
                Line.save(vm.line, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('modelBuilderApp:lineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
