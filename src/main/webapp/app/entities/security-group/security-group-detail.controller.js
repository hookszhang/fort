(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityGroupDetailController', SecurityGroupDetailController);

    SecurityGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityGroup', 'SecurityApp', 'SecurityUser'];

    function SecurityGroupDetailController($scope, $rootScope, $stateParams, entity, SecurityGroup, SecurityApp, SecurityUser) {
        var vm = this;
        vm.securityGroup = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityGroupUpdate', function(event, result) {
            vm.securityGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
