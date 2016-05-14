(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityGroupController', SecurityGroupController);

    SecurityGroupController.$inject = ['$scope', '$state', 'SecurityGroup', 'SecurityGroupSearch'];

    function SecurityGroupController ($scope, $state, SecurityGroup, SecurityGroupSearch) {
        var vm = this;
        vm.securityGroups = [];
        vm.loadAll = function() {
            SecurityGroup.query(function(result) {
                vm.securityGroups = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SecurityGroupSearch.query({query: vm.searchQuery}, function(result) {
                vm.securityGroups = result;
            });
        };
        vm.loadAll();
        
    }
})();
