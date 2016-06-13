(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityUserDialogController', SecurityUserDialogController);

    SecurityUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityUser', 'SecurityApp', 'SecurityRole', 'SecurityGroup'];

    function SecurityUserDialogController ($timeout, $scope, $stateParams, $state, SecurityUser, SecurityApp, SecurityRole, SecurityGroup) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityUser = SecurityUser.get({id : $stateParams.id}, function(){
                vm.securityUser.confirmpassword = vm.securityUser.passwordHash;
                // add firstLoad tag
                vm.securityUser.firstLoad = true;
            });
        } else {
            vm.securityUser = {
                activated: true
            }
        }
        vm.securityapps = SecurityApp.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.$watch('vm.securityUser.app', function(n, o, e) {
            // check firstLoad tag
            if (!vm.securityUser.firstLoad) {
                vm.securityUser.roles = [];
                vm.securityUser.groups = [];
            }
            if (n) {
                console.log(vm.securityUser);
                vm.securityroles = SecurityRole.query({appId: n.id});
                vm.securitygroups = SecurityGroup.query({appId: n.id});
                // change firstLoad tag to false
                vm.securityUser.firstLoad = false;
            }
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityUserUpdate', result);
            $state.go('security-user', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityUser.passwordHash && (vm.securityUser.passwordHash !== vm.securityUser.confirmpassword)) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                if (vm.securityUser.id !== null) {
                    SecurityUser.update(vm.securityUser, onSaveSuccess, onSaveError);
                } else {
                    SecurityUser.save(vm.securityUser, onSaveSuccess, onSaveError);
                }
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
