(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('LineDetailsSearch', LineDetailsSearch);

    LineDetailsSearch.$inject = ['$resource'];

    function LineDetailsSearch($resource) {
        var resourceUrl =  'api/_search/line-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
