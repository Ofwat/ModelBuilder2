(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('ModelExporter', ModelExporter);

    ModelExporter.$inject = ['$resource', 'DateUtils'];

    function ModelExporter($resource, DateUtils){
            return $resource('api/modelExport/:id', {}, {
                'export': {method: 'POST'}
            });
    }
})()
