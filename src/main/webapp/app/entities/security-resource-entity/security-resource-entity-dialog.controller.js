(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityResourceEntityDialogController', SecurityResourceEntityDialogController);

    SecurityResourceEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityResourceEntity', 'SecurityApp', 'SecurityAuthority'];

    function SecurityResourceEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityResourceEntity, SecurityApp, SecurityAuthority) {
        var vm = this;
        vm.securityResourceEntity = entity;
        vm.securityapps = SecurityApp.query();
        vm.securityauthorities = SecurityAuthority.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityResourceEntityUpdate', result);
            $uibModalInstance.close(result);
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
