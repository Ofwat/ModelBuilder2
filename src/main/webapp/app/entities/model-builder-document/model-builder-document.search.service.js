(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ModelBuilderDocumentSearch', ModelBuilderDocumentSearch);

    ModelBuilderDocumentSearch.$inject = ['$resource'];

    function ModelBuilderDocumentSearch($resource) {
        var resourceUrl =  'api/_search/model-builder-documents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
