(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('CellRange', CellRange);

    CellRange.$inject = ['$resource'];

    function CellRange ($resource) {
        var resourceUrl =  'api/cell-ranges/:id';

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
