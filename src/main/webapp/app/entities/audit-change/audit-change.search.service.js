(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('AuditChangeSearch', AuditChangeSearch);

    AuditChangeSearch.$inject = ['$resource'];

    function AuditChangeSearch($resource) {
        var resourceUrl =  'api/_search/audit-changes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
