(function() {
    'use strict';
    angular
        .module('fortApp')
        .factory('SecurityLoginEvent', SecurityLoginEvent);

    SecurityLoginEvent.$inject = ['$resource', 'DateUtils'];

    function SecurityLoginEvent ($resource, DateUtils) {
        var resourceUrl =  'api/security-login-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.tokenOverdueTime = DateUtils.convertDateTimeFromServer(data.tokenOverdueTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
