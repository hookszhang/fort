(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityAppSearch', SecurityAppSearch);

    SecurityAppSearch.$inject = ['$resource'];

    function SecurityAppSearch($resource) {
        var resourceUrl =  'api/_search/security-apps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
