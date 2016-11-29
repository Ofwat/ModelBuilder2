(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .controller('SectionDetailsController', SectionDetailsController);

    SectionDetailsController.$inject = ['$scope', '$state', 'SectionDetails', 'SectionDetailsSearch'];

    function SectionDetailsController ($scope, $state, SectionDetails, SectionDetailsSearch) {
        var vm = this;
        
        vm.sectionDetails = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SectionDetails.query(function(result) {
                vm.sectionDetails = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SectionDetailsSearch.query({query: vm.searchQuery}, function(result) {
                vm.sectionDetails = result;
            });
        }    }
})();
