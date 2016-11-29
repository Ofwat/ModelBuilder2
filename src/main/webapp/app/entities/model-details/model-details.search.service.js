(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ModelDetailsSearch', ModelDetailsSearch);

    ModelDetailsSearch.$inject = ['$resource'];

    function ModelDetailsSearch($resource) {
        var resourceUrl =  'api/_search/model-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
