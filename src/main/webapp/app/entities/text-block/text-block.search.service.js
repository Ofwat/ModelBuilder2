(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('TextBlockSearch', TextBlockSearch);

    TextBlockSearch.$inject = ['$resource'];

    function TextBlockSearch($resource) {
        var resourceUrl =  'api/_search/text-blocks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
