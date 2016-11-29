(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('LineDetails', LineDetails);

    LineDetails.$inject = ['$resource'];

    function LineDetails ($resource) {
        var resourceUrl =  'api/line-details/:id';

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
