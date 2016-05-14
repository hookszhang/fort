(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityLoginEventController', SecurityLoginEventController);

    SecurityLoginEventController.$inject = ['$scope', '$state', 'SecurityLoginEvent', 'SecurityLoginEventSearch'];

    function SecurityLoginEventController ($scope, $state, SecurityLoginEvent, SecurityLoginEventSearch) {
        var vm = this;
        vm.securityLoginEvents = [];
        vm.loadAll = function() {
            SecurityLoginEvent.query(function(result) {
                vm.securityLoginEvents = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SecurityLoginEventSearch.query({query: vm.searchQuery}, function(result) {
                vm.securityLoginEvents = result;
            });
        };
        vm.loadAll();
        
    }
})();
