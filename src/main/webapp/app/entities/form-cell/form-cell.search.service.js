(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('FormCellSearch', FormCellSearch);

    FormCellSearch.$inject = ['$resource'];

    function FormCellSearch($resource) {
        var resourceUrl =  'api/_search/form-cells/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
