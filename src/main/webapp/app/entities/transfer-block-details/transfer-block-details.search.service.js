(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TransferBlockDetailsSearch', TransferBlockDetailsSearch);

    TransferBlockDetailsSearch.$inject = ['$resource'];

    function TransferBlockDetailsSearch($resource) {
        var resourceUrl =  'api/_search/transfer-block-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
