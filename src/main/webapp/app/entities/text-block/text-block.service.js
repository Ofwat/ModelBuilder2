(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('TextBlock', TextBlock);

    TextBlock.$inject = ['$resource'];

    function TextBlock ($resource) {
        var resourceUrl =  'api/text-blocks/:id';

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
