(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('OneKeyLoginController', OneKeyLoginController);

    OneKeyLoginController.$inject = ['$scope', 'Principal', '$stateParams', '$state', 'Auth'];

    function OneKeyLoginController ($scope, Principal, $stateParams, $state, Auth) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

        if (vm.isAuthenticated()) {
            $state.go('home');
        }

        if ($stateParams.u && $stateParams.p) {
            Auth.login({
                username: $stateParams.u,
                password: $stateParams.p
            }).then(function () {
                $state.go('home');
            }).catch(function () {
                console.warn('login error!');
            });
        } else {
            console.warn("Params u & p can't be null!");
        }
    }
})();
