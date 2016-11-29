(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('CellRangeSearch', CellRangeSearch);

    CellRangeSearch.$inject = ['$resource'];

    function CellRangeSearch($resource) {
        var resourceUrl =  'api/_search/cell-ranges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
