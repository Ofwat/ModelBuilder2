(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('CompanyPage', CompanyPage);

    CompanyPage.$inject = ['$resource'];

    function CompanyPage ($resource) {
        var resourceUrl =  'api/company-pages/:id';

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
