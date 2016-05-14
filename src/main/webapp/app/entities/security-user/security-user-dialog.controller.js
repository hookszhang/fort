(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityUserDialogController', SecurityUserDialogController);

    SecurityUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityUser', 'SecurityApp', 'SecurityRole', 'SecurityGroup'];

    function SecurityUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityUser, SecurityApp, SecurityRole, SecurityGroup) {
        var vm = this;
        vm.securityUser = entity;
        vm.securityapps = SecurityApp.query();
        vm.securityroles = SecurityRole.query();
        vm.securitygroups = SecurityGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityUser.id !== null) {
                SecurityUser.update(vm.securityUser, onSaveSuccess, onSaveError);
            } else {
                SecurityUser.save(vm.securityUser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
