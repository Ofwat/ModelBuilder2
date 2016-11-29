(function() {
    'use strict';
    angular
        .module('modelBuilderApp')
        .factory('SectionDetails', SectionDetails);

    SectionDetails.$inject = ['$resource'];

    function SectionDetails ($resource) {
        var resourceUrl =  'api/section-details/:id';

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
