(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('Macro', Macro);

    Macro.$inject = ['$resource'];

    function Macro ($resource) {
        var resourceUrl =  'api/macros/:id';

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
