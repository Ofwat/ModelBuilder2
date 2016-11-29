(function() {

    'use strict';

    angular.module('modelBuilderApp').factory('ModelBuilder', ModelBuilder);

    ModelBuilder.$inject = ['$resource'];

        function ModelBuilder($resource) {
            return $resource('api/modelDetailss/:id', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': {method: 'PUT'},
                'copy': {
                    method: 'PUT',
                    url: 'api/models/copy/:id',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                }
            });
        }
})()
