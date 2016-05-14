(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityLoginEventDialogController', SecurityLoginEventDialogController);

    SecurityLoginEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityLoginEvent', 'SecurityUser'];

    function SecurityLoginEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityLoginEvent, SecurityUser) {
        var vm = this;
        vm.securityLoginEvent = entity;
        vm.securityusers = SecurityUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityLoginEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityLoginEvent.id !== null) {
                SecurityLoginEvent.update(vm.securityLoginEvent, onSaveSuccess, onSaveError);
            } else {
                SecurityLoginEvent.save(vm.securityLoginEvent, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.tokenOverdueTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
