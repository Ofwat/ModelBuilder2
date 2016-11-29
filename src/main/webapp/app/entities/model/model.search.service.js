(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ModelSearch', ModelSearch);

    ModelSearch.$inject = ['$resource'];

    function ModelSearch($resource) {
        var resourceUrl =  'api/_search/models/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
