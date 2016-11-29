(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('InputSearch', InputSearch);

    InputSearch.$inject = ['$resource'];

    function InputSearch($resource) {
        var resourceUrl =  'api/_search/inputs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
