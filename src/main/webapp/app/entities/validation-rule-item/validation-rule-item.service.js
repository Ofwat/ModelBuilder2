(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ValidationRuleItem', ValidationRuleItem);

    ValidationRuleItem.$inject = ['$resource'];

    function ValidationRuleItem ($resource) {
        var resourceUrl =  'api/validation-rule-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
