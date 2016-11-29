(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('TransferCondition', TransferCondition);

    TransferCondition.$inject = ['$resource'];

    function TransferCondition ($resource) {
        var resourceUrl =  'api/transfer-conditions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
