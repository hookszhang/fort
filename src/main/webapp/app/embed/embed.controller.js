(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('EmbedController', EmbedController);

    EmbedController.$inject = ['$scope', 'Principal', '$stateParams', '$state', 'Auth', '$rootScope'];

    function EmbedController($scope, Principal, $stateParams, $state, Auth, $rootScope) {
        var vm = this;
        $rootScope.enable_embedded_mode = true;

        // validate
        if (!$stateParams.u) {
            vm.tip = 'The param "u"(username) can\'t be null!';
            return;
        } else if (!$stateParams.p) {
            vm.tip = 'The param "p"(password) can\'t be null!';
            return;
        } else if (!$stateParams.m) {
            vm.tip = 'The param "m"(model) can\'t be null!';
            return;
        }

        if (Principal.isAuthenticated()) {
            $state.go($stateParams.m);
            return;
        }

        Auth.login({
            username: $stateParams.u,
            password: $stateParams.p
        }).then(function() {
            $state.go($stateParams.m);
        }).catch(function() {
            vm.tip = 'Embed error, 401 the bad username or password!';
        });
    }
})();
