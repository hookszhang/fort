(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityNavDialogController', SecurityNavDialogController);

    SecurityNavDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityNav', 'SecurityResourceEntity', 'SecurityApp'];

    function SecurityNavDialogController ($timeout, $scope, $stateParams, $state, SecurityNav, SecurityResourceEntity, SecurityApp) {
        var vm = this;
        // vm.securityNav = entity;
        if ($stateParams.id) {
            vm.securityNav = SecurityNav.get({id : $stateParams.id});
        }
        vm.securitynavs = SecurityNav.query(function() {
            if ($stateParams.id) {
                // prevent extends loop
                for (var i in vm.securitynavs) {
                    var nav = vm.securitynavs[i];
                    if ($stateParams.id == nav.id) {
                        // remove this nav.
                        vm.securitynavs.splice(i, 1);
                    }
                }
            }
        });
        vm.securityresourceentities = SecurityResourceEntity.query();
        vm.securityapps = SecurityApp.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityNavUpdate', result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityNav.id !== null) {
                SecurityNav.update(vm.securityNav, onSaveSuccess, onSaveError);
            } else {
                SecurityNav.save(vm.securityNav, onSaveSuccess, onSaveError);
            }
            $state.go('security-nav', null, { reload: true });
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
