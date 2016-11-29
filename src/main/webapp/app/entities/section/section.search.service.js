(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('SectionSearch', SectionSearch);

    SectionSearch.$inject = ['$resource'];

    function SectionSearch($resource) {
        var resourceUrl =  'api/_search/sections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
