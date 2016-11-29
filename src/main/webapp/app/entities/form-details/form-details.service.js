(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('FormDetails', FormDetails);

    FormDetails.$inject = ['$resource'];

    function FormDetails ($resource) {
        var resourceUrl =  'api/form-details/:id';

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
