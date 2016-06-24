(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAppDialogController', SecurityAppDialogController);

    SecurityAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityApp'];

    function SecurityAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityApp) {
        var vm = this;
        vm.securityApp = entity;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityAppUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityApp.id !== null) {
                SecurityApp.update(vm.securityApp, onSaveSuccess, onSaveError);
            } else {
                SecurityApp.save(vm.securityApp, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
