(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('PageSearch', PageSearch);

    PageSearch.$inject = ['$resource'];

    function PageSearch($resource) {
        var resourceUrl =  'api/_search/pages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
