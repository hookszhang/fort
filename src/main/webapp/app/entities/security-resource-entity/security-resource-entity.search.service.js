(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityResourceEntitySearch', SecurityResourceEntitySearch);

    SecurityResourceEntitySearch.$inject = ['$resource'];

    function SecurityResourceEntitySearch($resource) {
        var resourceUrl =  'api/_search/security-resource-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
