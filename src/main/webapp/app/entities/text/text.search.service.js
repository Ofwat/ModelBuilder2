(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TextSearch', TextSearch);

    TextSearch.$inject = ['$resource'];

    function TextSearch($resource) {
        var resourceUrl =  'api/_search/texts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
