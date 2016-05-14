(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityGroup', SecurityGroup);

    SecurityGroup.$inject = ['$resource'];

    function SecurityGroup ($resource) {
        var resourceUrl =  'api/security-groups/:id';

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
