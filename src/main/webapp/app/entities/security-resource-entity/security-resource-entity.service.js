(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityResourceEntity', SecurityResourceEntity);

    SecurityResourceEntity.$inject = ['$resource'];

    function SecurityResourceEntity ($resource) {
        var resourceUrl =  'api/security-resource-entities/:id';

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
