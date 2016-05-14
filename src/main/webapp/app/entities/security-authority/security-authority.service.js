(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityAuthority', SecurityAuthority);

    SecurityAuthority.$inject = ['$resource'];

    function SecurityAuthority ($resource) {
        var resourceUrl =  'api/security-authorities/:id';

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
