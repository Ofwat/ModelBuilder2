(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('TextController', TextController);

    TextController.$inject = ['$scope', '$state', 'Text', 'TextSearch'];

    function TextController ($scope, $state, Text, TextSearch) {
        var vm = this;
        
        vm.texts = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Text.query(function(result) {
                vm.texts = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TextSearch.query({query: vm.searchQuery}, function(result) {
                vm.texts = result;
            });
        }    }
})();
