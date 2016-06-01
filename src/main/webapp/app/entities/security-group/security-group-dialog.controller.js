(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityGroupDialogController', SecurityGroupDialogController);

    SecurityGroupDialogController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'SecurityGroup', 'SecurityApp', 'SecurityUser'];

    function SecurityGroupDialogController ($timeout, $scope, $state, $stateParams, SecurityGroup, SecurityApp, SecurityUser) {
        var vm = this;
        if ($stateParams.id) {
             vm.securityGroup = SecurityGroup.get({id : $stateParams.id});
        }
        vm.securityapps = SecurityApp.query();
        vm.securityusers = SecurityUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityGroupUpdate', result);
            $state.go('security-group', null, { reload: true });
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
