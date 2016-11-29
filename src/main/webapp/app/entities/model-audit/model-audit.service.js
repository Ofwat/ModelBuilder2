(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ModelAudit', ModelAudit);

    ModelAudit.$inject = ['$resource'];

    function ModelAudit ($resource) {
        var resourceUrl =  'api/model-audits/:id';

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
