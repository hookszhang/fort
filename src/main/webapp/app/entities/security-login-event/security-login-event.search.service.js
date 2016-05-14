(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityLoginEventSearch', SecurityLoginEventSearch);

    SecurityLoginEventSearch.$inject = ['$resource'];

    function SecurityLoginEventSearch($resource) {
        var resourceUrl =  'api/_search/security-login-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
