(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('Input', Input);

    Input.$inject = ['$resource'];

    function Input ($resource) {
        var resourceUrl =  'api/inputs/:id';

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
