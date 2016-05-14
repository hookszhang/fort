(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAppDetailController', SecurityAppDetailController);

    SecurityAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SecurityApp'];

    function SecurityAppDetailController($scope, $rootScope, $stateParams, entity, SecurityApp) {
        var vm = this;
        vm.securityApp = entity;
        
        var unsubscribe = $rootScope.$on('fortApp:securityAppUpdate', function(event, result) {
            vm.securityApp = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
