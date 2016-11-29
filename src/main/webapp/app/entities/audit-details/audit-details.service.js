(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('AuditDetails', AuditDetails);

    AuditDetails.$inject = ['$resource'];

    function AuditDetails ($resource) {
        var resourceUrl =  'api/audit-details/:id';

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
