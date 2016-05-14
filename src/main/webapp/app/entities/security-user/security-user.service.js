(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityUser', SecurityUser);

    SecurityUser.$inject = ['$resource'];

    function SecurityUser ($resource) {
        var resourceUrl =  'api/security-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
