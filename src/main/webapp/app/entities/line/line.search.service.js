(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('LineSearch', LineSearch);

    LineSearch.$inject = ['$resource'];

    function LineSearch($resource) {
        var resourceUrl =  'api/_search/lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
