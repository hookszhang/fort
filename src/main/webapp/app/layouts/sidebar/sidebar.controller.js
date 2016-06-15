(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['Principal'];

    function SidebarController (Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
    }
})();
