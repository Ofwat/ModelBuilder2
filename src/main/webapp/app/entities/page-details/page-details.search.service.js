(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('PageDetailsSearch', PageDetailsSearch);

    PageDetailsSearch.$inject = ['$resource'];

    function PageDetailsSearch($resource) {
        var resourceUrl =  'api/_search/page-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
