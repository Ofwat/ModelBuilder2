(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('fountainModelService', fountainModelService);

    fountainModelService.$inject = ['$rootScope', '$http'];


    function fountainModelService($rootScope, $http) {
        return {
            getModelFamilies: function () {
                /*
                 return $http.get('metrics/metrics').then(function (response) {
                 return response.data;
                 });
                 */
                return [
                    {"id": "2", "name": "June Return", "code": "JR"},
                    {"id": "3", "name": "Principal Statements", "code": "PS"},
                    {"id": "4", "name": "Price Limit", "code": "PL"},
                    {"id": "5", "name": "August Submission", "code": "AS"},
                    {"id": "6", "name": "Blind year reconciliation", "code": "BY"},
                    {"id": "6", "name": "Periodic Review 2014", "code": "PR14"},
                    {"id": "7", "name": "June Return", "code": "JR"}
                ];
            },

            getRuns: function () {
                return [
                    {"name": "Default for Cyclical base run", "code": "Cyclical_106"},
                    {"name": "Periodic Review 14 Data", "code": "PR14_22"},
                    {"name": "Wholesale Indicative Baselines", "code": "PR14_23"},
                    {"name": "Cyclical Base Run", "code": "Cyclical_96"},
                    {"name": "PR14 Blind year reconciliation ", "code": "PR14_95"},
                    {"name": "Run 10: Final Determinations", "code": "PR14_71"},
                    {"name": "Accounting Separation 2011-12", "code": "Cyclical_67"}
                ];
            },

            getModelTypes: function () {
                return [
                    {"id": "1", "name": "ICS"},
                    {"id": "2", "name": "REPORT"},
                    {"id": "3", "name": "SCENARIO"},
                    {"id": "4", "name": "FOUNDATION"},
                    {"id": "5", "name": "EXTERNAL"},
                    {"id": "6", "name": "TAG_POINT"},
                    {"id": "7", "name": "ACC_SEP"}
                ];
            }
        };
        /*return $resource('api/modelDetailss/:id', {}, {
         'query': { method: 'GET', isArray: true},
         'get': {
         method: 'GET',
         transformResponse: function (data) {
         data = angular.fromJson(data);
         return data;
         }
         },
         'update': { method:'PUT' }
         });*/
        //Return multiple resources example
        /*
         factory("geoProvider", function($resource) {
         return {
         states: $resource('../data/states.json', {}, {
         query: { method: 'GET', params: {}, isArray: false }
         }),
         countries: $resource('../data/countries.json', {}, {
         query: { method: 'GET', params: {}, isArray: false }
         })
         };
         */
    }
})()
