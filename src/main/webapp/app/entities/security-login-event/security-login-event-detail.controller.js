(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityLoginEventDetailController', SecurityLoginEventDetailController);

    SecurityLoginEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityLoginEvent', 'SecurityUser'];

    function SecurityLoginEventDetailController($scope, $rootScope, $stateParams, entity, SecurityLoginEvent, SecurityUser) {
        var vm = this;
        vm.securityLoginEvent = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityLoginEventUpdate', function(event, result) {
            vm.securityLoginEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
