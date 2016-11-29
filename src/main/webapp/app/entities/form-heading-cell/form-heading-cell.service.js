(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('FormHeadingCell', FormHeadingCell);

    FormHeadingCell.$inject = ['$resource'];

    function FormHeadingCell ($resource) {
        var resourceUrl =  'api/form-heading-cells/:id';

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
