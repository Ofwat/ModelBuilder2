(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TransferBlockItemSearch', TransferBlockItemSearch);

    TransferBlockItemSearch.$inject = ['$resource'];

    function TransferBlockItemSearch($resource) {
        var resourceUrl =  'api/_search/transfer-block-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
