(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('ModelBuilderDocumentController', ModelBuilderDocumentController);

    ModelBuilderDocumentController.$inject = ['$scope', '$state', 'ModelBuilderDocument', 'ModelBuilderDocumentSearch'];

    function ModelBuilderDocumentController ($scope, $state, ModelBuilderDocument, ModelBuilderDocumentSearch) {
        var vm = this;
        
        vm.modelBuilderDocuments = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelBuilderDocument.query(function(result) {
                vm.modelBuilderDocuments = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelBuilderDocumentSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelBuilderDocuments = result;
            });
        }    }
})();
