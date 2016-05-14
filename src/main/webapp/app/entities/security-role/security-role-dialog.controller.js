(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityRoleDialogController', SecurityRoleDialogController);

    SecurityRoleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityRole', 'SecurityApp', 'SecurityAuthority', 'SecurityUser'];

    function SecurityRoleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityRole, SecurityApp, SecurityAuthority, SecurityUser) {
        var vm = this;
        vm.securityRole = entity;
        vm.securityapps = SecurityApp.query();
        vm.securityauthorities = SecurityAuthority.query();
        vm.securityusers = SecurityUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityRoleUpdate', result);
            $uibModalInstance.close(result);
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
