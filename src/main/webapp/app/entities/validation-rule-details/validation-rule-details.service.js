(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('ValidationRuleDetails', ValidationRuleDetails);

    ValidationRuleDetails.$inject = ['$resource'];

    function ValidationRuleDetails ($resource) {
        var resourceUrl =  'api/validation-rule-details/:id';

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
