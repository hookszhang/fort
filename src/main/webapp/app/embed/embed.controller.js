(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('EmbedController', EmbedController);

    EmbedController.$inject = ['$scope', 'Principal', '$stateParams', '$state', 'Auth', '$rootScope'];

    function EmbedController ($scope, Principal, $stateParams, $state, Auth, $rootScope) {
        $rootScope.enable_embedded_mode = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        if (vm.isAuthenticated()) {
            // $state.go('home');
            $state.go($stateParams.m);
            return;
        }

        if ($stateParams.u && $stateParams.p) {
            Auth.login({
                username: $stateParams.u,
                password: $stateParams.p
            }).then(function () {
                // $state.go('home');
            }).catch(function () {
                console.warn('login error!');
            });
        } else {
            console.warn("Params u & p can't be null!");
        }
    }
})();
