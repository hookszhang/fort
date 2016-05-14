(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityGroupDialogController', SecurityGroupDialogController);

    SecurityGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityGroup', 'SecurityApp', 'SecurityUser'];

    function SecurityGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityGroup, SecurityApp, SecurityUser) {
        var vm = this;
        vm.securityGroup = entity;
        vm.securityapps = SecurityApp.query();
        vm.securityusers = SecurityUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityGroup.id !== null) {
                SecurityGroup.update(vm.securityGroup, onSaveSuccess, onSaveError);
            } else {
                SecurityGroup.save(vm.securityGroup, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
