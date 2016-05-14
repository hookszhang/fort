(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityNavDialogController', SecurityNavDialogController);

    SecurityNavDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'SecurityNav', 'SecurityResourceEntity'];

    function SecurityNavDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, SecurityNav, SecurityResourceEntity) {
        var vm = this;
        vm.securityNav = entity;
        vm.parents = SecurityNav.query({filter: 'securitynav-is-null'});
        $q.all([vm.securityNav.$promise, vm.parents.$promise]).then(function() {
            if (!vm.securityNav.parent || !vm.securityNav.parent.id) {
                return $q.reject();
            }
            return SecurityNav.get({id : vm.securityNav.parent.id}).$promise;
        }).then(function(parent) {
            vm.parents.push(parent);
        });
        vm.resources = SecurityResourceEntity.query({filter: 'securitynav-is-null'});
        $q.all([vm.securityNav.$promise, vm.resources.$promise]).then(function() {
            if (!vm.securityNav.resource || !vm.securityNav.resource.id) {
                return $q.reject();
            }
            return SecurityResourceEntity.get({id : vm.securityNav.resource.id}).$promise;
        }).then(function(resource) {
            vm.resources.push(resource);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityNavUpdate', result);
            $uibModalInstance.close(result);
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
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
