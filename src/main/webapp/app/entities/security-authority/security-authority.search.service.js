(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityAuthoritySearch', SecurityAuthoritySearch);

    SecurityAuthoritySearch.$inject = ['$resource'];

    function SecurityAuthoritySearch($resource) {
        var resourceUrl =  'api/_search/security-authorities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
