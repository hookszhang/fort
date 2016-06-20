(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityResourceEntityDialogController', SecurityResourceEntityDialogController);

    SecurityResourceEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityResourceEntity', 'SecurityApp', 'SecurityAuthority', 'Principal'];

    function SecurityResourceEntityDialogController ($timeout, $scope, $stateParams, $state, SecurityResourceEntity, SecurityApp, SecurityAuthority, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityResourceEntity = SecurityResourceEntity.get({id : $stateParams.id});
        }
        vm.securityapps = SecurityApp.query(function(data) {
            // Auto select
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityResourceEntity = {app: data[0]};
            }
        });
        vm.securityauthorities = SecurityAuthority.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityResourceEntityUpdate', result);
            $state.go('security-resource-entity', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityResourceEntity.id !== null) {
                SecurityResourceEntity.update(vm.securityResourceEntity, onSaveSuccess, onSaveError);
            } else {
                SecurityResourceEntity.save(vm.securityResourceEntity, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
