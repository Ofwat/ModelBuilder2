(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('CompanyPageSearch', CompanyPageSearch);

    CompanyPageSearch.$inject = ['$resource'];

    function CompanyPageSearch($resource) {
        var resourceUrl =  'api/_search/company-pages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
