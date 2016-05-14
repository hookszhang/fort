(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityResourceEntityDetailController', SecurityResourceEntityDetailController);

    SecurityResourceEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityResourceEntity', 'SecurityApp', 'SecurityAuthority'];

    function SecurityResourceEntityDetailController($scope, $rootScope, $stateParams, entity, SecurityResourceEntity, SecurityApp, SecurityAuthority) {
        var vm = this;
        vm.securityResourceEntity = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityResourceEntityUpdate', function(event, result) {
            vm.securityResourceEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
