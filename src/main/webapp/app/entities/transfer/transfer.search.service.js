(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TransferSearch', TransferSearch);

    TransferSearch.$inject = ['$resource'];

    function TransferSearch($resource) {
        var resourceUrl =  'api/_search/transfers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
