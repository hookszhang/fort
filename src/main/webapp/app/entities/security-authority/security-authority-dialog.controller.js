(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAuthorityDialogController', SecurityAuthorityDialogController);
    SecurityAuthorityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityAuthority', 'SecurityApp', 'SecurityResourceEntity', 'SecurityRole'];

    function SecurityAuthorityDialogController($timeout, $scope, $stateParams, $state, SecurityAuthority, SecurityApp, SecurityResourceEntity, SecurityRole) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityAuthority = SecurityAuthority.get({id : $stateParams.id});
        }

        vm.securityapps = SecurityApp.query();
        vm.securityroles = SecurityRole.query();

        $timeout(function() {
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function(result) {
            $scope.$emit('fortApp:securityAuthorityUpdate', result);
            $state.go('security-authority', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function() {
            vm.isSaving = false;
            $state.go('security-authority');
        };

        // add watch app, search resource entity when app change
        $scope.$watch('vm.securityAuthority.app', function(n, o, e) {
            if (n) {
                vm.securityresourceentities = SecurityResourceEntity.query({appId: n.id});
            }
            // if (vm.securityAuthority) {
            //     vm.securityAuthority.resources = [];
            // }
        });

        vm.save = function() {
            vm.isSaving = true;
            if (vm.securityAuthority.id !== null) {
                SecurityAuthority.update(vm.securityAuthority, onSaveSuccess, onSaveError);
            } else {
                SecurityAuthority.save(vm.securityAuthority, onSaveSuccess, onSaveError);
            }
        };
    }
})();
