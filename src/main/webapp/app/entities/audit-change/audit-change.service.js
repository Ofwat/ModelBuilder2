(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('AuditChange', AuditChange);

    AuditChange.$inject = ['$resource'];

    function AuditChange ($resource) {
        var resourceUrl =  'api/audit-changes/:id';

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
