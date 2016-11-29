(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ValidationRuleItemSearch', ValidationRuleItemSearch);

    ValidationRuleItemSearch.$inject = ['$resource'];

    function ValidationRuleItemSearch($resource) {
        var resourceUrl =  'api/_search/validation-rule-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
