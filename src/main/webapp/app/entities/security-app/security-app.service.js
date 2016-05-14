(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityApp', SecurityApp);

    SecurityApp.$inject = ['$resource'];

    function SecurityApp ($resource) {
        var resourceUrl =  'api/security-apps/:id';

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
