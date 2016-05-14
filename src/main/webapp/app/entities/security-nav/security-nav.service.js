(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityNav', SecurityNav);

    SecurityNav.$inject = ['$resource'];

    function SecurityNav ($resource) {
        var resourceUrl =  'api/security-navs/:id';

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
