(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TransferBlockSearch', TransferBlockSearch);

    TransferBlockSearch.$inject = ['$resource'];

    function TransferBlockSearch($resource) {
        var resourceUrl =  'api/_search/transfer-blocks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
