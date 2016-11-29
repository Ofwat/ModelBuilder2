(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('SectionDetailsSearch', SectionDetailsSearch);

    SectionDetailsSearch.$inject = ['$resource'];

    function SectionDetailsSearch($resource) {
        var resourceUrl =  'api/_search/section-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
