(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('YearCode', YearCode);

    YearCode.$inject = ['$resource'];

    function YearCode ($resource) {
        var resourceUrl =  'api/year-codes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
