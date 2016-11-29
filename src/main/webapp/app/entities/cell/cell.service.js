(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('Cell', Cell);

    Cell.$inject = ['$resource'];

    function Cell ($resource) {
        var resourceUrl =  'api/cells/:id';

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
