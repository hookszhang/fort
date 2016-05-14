(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityUserDetailController', SecurityUserDetailController);

    SecurityUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityUser', 'SecurityApp', 'SecurityRole', 'SecurityGroup'];

    function SecurityUserDetailController($scope, $rootScope, $stateParams, entity, SecurityUser, SecurityApp, SecurityRole, SecurityGroup) {
        var vm = this;
        vm.securityUser = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityUserUpdate', function(event, result) {
            vm.securityUser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
