(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('TransferBlockItem', TransferBlockItem);

    TransferBlockItem.$inject = ['$resource'];

    function TransferBlockItem ($resource) {
        var resourceUrl =  'api/transfer-block-items/:id';

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
