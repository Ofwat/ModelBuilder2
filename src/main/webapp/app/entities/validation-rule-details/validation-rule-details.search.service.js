(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ValidationRuleDetailsSearch', ValidationRuleDetailsSearch);

    ValidationRuleDetailsSearch.$inject = ['$resource'];

    function ValidationRuleDetailsSearch($resource) {
        var resourceUrl =  'api/_search/validation-rule-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
