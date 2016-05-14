(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityGroupSearch', SecurityGroupSearch);

    SecurityGroupSearch.$inject = ['$resource'];

    function SecurityGroupSearch($resource) {
        var resourceUrl =  'api/_search/security-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
