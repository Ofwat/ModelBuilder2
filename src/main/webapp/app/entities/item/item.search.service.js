(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ItemSearch', ItemSearch);

    ItemSearch.$inject = ['$resource'];

    function ItemSearch($resource) {
        var resourceUrl =  'api/_search/items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
