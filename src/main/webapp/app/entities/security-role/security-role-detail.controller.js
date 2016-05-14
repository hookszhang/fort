(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityRoleDetailController', SecurityRoleDetailController);

    SecurityRoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityRole', 'SecurityApp', 'SecurityAuthority', 'SecurityUser'];

    function SecurityRoleDetailController($scope, $rootScope, $stateParams, entity, SecurityRole, SecurityApp, SecurityAuthority, SecurityUser) {
        var vm = this;
        vm.securityRole = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityRoleUpdate', function(event, result) {
            vm.securityRole = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
