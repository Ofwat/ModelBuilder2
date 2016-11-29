(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('YearSearch', YearSearch);

    YearSearch.$inject = ['$resource'];

    function YearSearch($resource) {
        var resourceUrl =  'api/_search/years/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
