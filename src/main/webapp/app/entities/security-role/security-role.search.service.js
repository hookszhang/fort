(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityRoleSearch', SecurityRoleSearch);

    SecurityRoleSearch.$inject = ['$resource'];

    function SecurityRoleSearch($resource) {
        var resourceUrl =  'api/_search/security-roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
