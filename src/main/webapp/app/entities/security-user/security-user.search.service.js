(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityUserSearch', SecurityUserSearch);

    SecurityUserSearch.$inject = ['$resource'];

    function SecurityUserSearch($resource) {
        var resourceUrl =  'api/_search/security-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
