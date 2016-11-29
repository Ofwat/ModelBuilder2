(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('AuditDetailsSearch', AuditDetailsSearch);

    AuditDetailsSearch.$inject = ['$resource'];

    function AuditDetailsSearch($resource) {
        var resourceUrl =  'api/_search/audit-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
