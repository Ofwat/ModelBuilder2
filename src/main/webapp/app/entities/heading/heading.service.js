(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('Heading', Heading);

    Heading.$inject = ['$resource'];

    function Heading ($resource) {
        var resourceUrl =  'api/headings/:id';

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
