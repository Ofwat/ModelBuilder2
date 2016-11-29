(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ValidationRule', ValidationRule);

    ValidationRule.$inject = ['$resource'];

    function ValidationRule ($resource) {
        var resourceUrl =  'api/validation-rules/:id';

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
