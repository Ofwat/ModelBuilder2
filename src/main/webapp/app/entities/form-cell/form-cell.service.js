(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('FormCell', FormCell);

    FormCell.$inject = ['$resource'];

    function FormCell ($resource) {
        var resourceUrl =  'api/form-cells/:id';

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
