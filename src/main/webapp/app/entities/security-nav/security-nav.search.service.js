(function() {
    'use strict';

    angular
        .module('fortApp')
        .factory('SecurityNavSearch', SecurityNavSearch);

    SecurityNavSearch.$inject = ['$resource'];

    function SecurityNavSearch($resource) {
        var resourceUrl =  'api/_search/security-navs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
