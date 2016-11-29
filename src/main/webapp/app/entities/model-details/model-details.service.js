(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ModelDetails', ModelDetails);

    ModelDetails.$inject = ['$resource', 'DateUtils'];

    function ModelDetails($resource, DateUtils) {
        var resourceUrl =  'api/model-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastModified = DateUtils.convertDateTimeFromServer(data.lastModified);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
