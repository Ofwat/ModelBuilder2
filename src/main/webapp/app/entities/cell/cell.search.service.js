(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('CellSearch', CellSearch);

    CellSearch.$inject = ['$resource'];

    function CellSearch($resource) {
        var resourceUrl =  'api/_search/cells/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
