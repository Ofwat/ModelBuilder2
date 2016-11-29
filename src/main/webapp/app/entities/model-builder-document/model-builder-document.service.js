(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ModelBuilderDocument', ModelBuilderDocument);

    ModelBuilderDocument.$inject = ['$resource'];

    function ModelBuilderDocument ($resource) {
        var resourceUrl =  'api/model-builder-documents/:id';

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
