(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('HeadingSearch', HeadingSearch);

    HeadingSearch.$inject = ['$resource'];

    function HeadingSearch($resource) {
        var resourceUrl =  'api/_search/headings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
