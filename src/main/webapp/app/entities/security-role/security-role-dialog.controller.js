(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityRoleDialogController', SecurityRoleDialogController);

    SecurityRoleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityRole', 'SecurityApp', 'SecurityAuthority', 'SecurityUser'];

    function SecurityRoleDialogController ($timeout, $scope, $stateParams, $state, SecurityRole, SecurityApp, SecurityAuthority, SecurityUser) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityRole = SecurityRole.get({id : $stateParams.id}, function(){
                // add firstLoad tag
                vm.securityRole.firstLoad = true;
            });
        } else {
            vm.securityRole = {};
        }
        vm.securityapps = SecurityApp.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.$watch('vm.securityRole.app', function(n, o, e) {
            if (!vm.securityRole.firstLoad) {
                vm.securityRole.authorities = [];
            }
            if (n) {
                vm.securityauthorities = SecurityAuthority.query({appId: n.id});
                // change firstLoad tag to false
                vm.securityRole.firstLoad = false;
            }
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityRoleUpdate', result);
            $state.go('security-role', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityRole.id !== null) {
                SecurityRole.update(vm.securityRole, onSaveSuccess, onSaveError);
            } else {
                SecurityRole.save(vm.securityRole, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
