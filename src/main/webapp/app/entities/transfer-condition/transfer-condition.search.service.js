(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TransferConditionSearch', TransferConditionSearch);

    TransferConditionSearch.$inject = ['$resource'];

    function TransferConditionSearch($resource) {
        var resourceUrl =  'api/_search/transfer-conditions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
