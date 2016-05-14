(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityRole', SecurityRole);

    SecurityRole.$inject = ['$resource'];

    function SecurityRole ($resource) {
        var resourceUrl =  'api/security-roles/:id';

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
