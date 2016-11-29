(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ModelAuditSearch', ModelAuditSearch);

    ModelAuditSearch.$inject = ['$resource'];

    function ModelAuditSearch($resource) {
        var resourceUrl =  'api/_search/model-audits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
