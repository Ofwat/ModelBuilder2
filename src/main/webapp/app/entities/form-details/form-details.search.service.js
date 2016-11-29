(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('FormDetailsSearch', FormDetailsSearch);

    FormDetailsSearch.$inject = ['$resource'];

    function FormDetailsSearch($resource) {
        var resourceUrl =  'api/_search/form-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
