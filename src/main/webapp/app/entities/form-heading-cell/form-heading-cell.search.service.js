(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('FormHeadingCellSearch', FormHeadingCellSearch);

    FormHeadingCellSearch.$inject = ['$resource'];

    function FormHeadingCellSearch($resource) {
        var resourceUrl =  'api/_search/form-heading-cells/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
