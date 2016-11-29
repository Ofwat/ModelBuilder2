(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('TransferBlockDetails', TransferBlockDetails);

    TransferBlockDetails.$inject = ['$resource'];

    function TransferBlockDetails ($resource) {
        var resourceUrl =  'api/transfer-block-details/:id';

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
