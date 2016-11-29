(function() {
    'use strict';

    angular
        .module('modelBuilderApp')
        .factory('MacroSearch', MacroSearch);

    MacroSearch.$inject = ['$resource'];

    function MacroSearch($resource) {
        var resourceUrl =  'api/_search/macros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
