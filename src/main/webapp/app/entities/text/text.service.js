(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('Text', Text);

    Text.$inject = ['$resource'];

    function Text ($resource) {
        var resourceUrl =  'api/texts/:id';

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
