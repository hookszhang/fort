(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('OneKeyLoginController', OneKeyLoginController);

    OneKeyLoginController.$inject = ['$scope', 'Principal', '$stateParams', '$state', 'Auth', '$rootScope'];

    function OneKeyLoginController ($scope, Principal, $stateParams, $state, Auth, $rootScope) {
        $rootScope.enable_embedded_mode = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        console.log(vm.isAuthenticated());
        if (vm.isAuthenticated()) {
            // $state.go('home');
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
