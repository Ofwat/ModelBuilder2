(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('YearCodeSearch', YearCodeSearch);

    YearCodeSearch.$inject = ['$resource'];

    function YearCodeSearch($resource) {
        var resourceUrl =  'api/_search/year-codes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
