(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('PageDetails', PageDetails);

    PageDetails.$inject = ['$resource'];

    function PageDetails ($resource) {
        var resourceUrl =  'api/page-details/:id';

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
