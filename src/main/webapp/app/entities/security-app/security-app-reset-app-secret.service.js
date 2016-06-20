(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityAppResetAppSecret', SecurityAppResetAppSecret);

    SecurityAppResetAppSecret.$inject = ['$resource'];

    function SecurityAppResetAppSecret ($resource) {
        var resourceUrl =  'api/security-apps/reset-app-secret';

        return $resource(resourceUrl, {}, {
            'update': { method:'PUT' }
        });
    }
})();
