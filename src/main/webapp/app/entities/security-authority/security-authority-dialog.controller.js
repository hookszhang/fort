(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAuthorityDialogController', SecurityAuthorityDialogController);

    SecurityAuthorityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityAuthority', 'SecurityApp', 'SecurityResourceEntity', 'SecurityRole'];

    function SecurityAuthorityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityAuthority, SecurityApp, SecurityResourceEntity, SecurityRole) {
        var vm = this;
        vm.securityAuthority = entity;
        vm.securityapps = SecurityApp.query();
        vm.securityresourceentities = SecurityResourceEntity.query();
        vm.securityroles = SecurityRole.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityAuthorityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityAuthority.id !== null) {
                SecurityAuthority.update(vm.securityAuthority, onSaveSuccess, onSaveError);
            } else {
                SecurityAuthority.save(vm.securityAuthority, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
