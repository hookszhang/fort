(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAuthorityDetailController', SecurityAuthorityDetailController);

    SecurityAuthorityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityAuthority', 'SecurityApp', 'SecurityResourceEntity', 'SecurityRole'];

    function SecurityAuthorityDetailController($scope, $rootScope, $stateParams, entity, SecurityAuthority, SecurityApp, SecurityResourceEntity, SecurityRole) {
        var vm = this;
        vm.securityAuthority = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityAuthorityUpdate', function(event, result) {
            vm.securityAuthority = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
