(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityNavDetailController', SecurityNavDetailController);

    SecurityNavDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityNav', 'SecurityResourceEntity', 'SecurityApp'];

    function SecurityNavDetailController($scope, $rootScope, $stateParams, entity, SecurityNav, SecurityResourceEntity, SecurityApp) {
        var vm = this;
        vm.securityNav = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityNavUpdate', function(event, result) {
            vm.securityNav = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
