(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ValidationRuleSearch', ValidationRuleSearch);

    ValidationRuleSearch.$inject = ['$resource'];

    function ValidationRuleSearch($resource) {
        var resourceUrl =  'api/_search/validation-rules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
