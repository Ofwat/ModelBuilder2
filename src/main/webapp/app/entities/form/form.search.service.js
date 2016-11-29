(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('FormSearch', FormSearch);

    FormSearch.$inject = ['$resource'];

    function FormSearch($resource) {
        var resourceUrl =  'api/_search/forms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
