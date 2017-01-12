/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityUserDialogController', SecurityUserDialogController);

    SecurityUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityUser', 'SecurityApp', 'SecurityRole', 'SecurityGroup', 'Principal'];

    function SecurityUserDialogController ($timeout, $scope, $stateParams, $state, SecurityUser, SecurityApp, SecurityRole, SecurityGroup, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityUser = SecurityUser.get({id : $stateParams.id}, function(){
                vm.securityUser.confirmpassword = vm.securityUser.passwordHash;
                // add firstLoad tag
                vm.securityUser.firstLoad = true;
            });
        } else {
            vm.securityUser = {
                activated: true
            }
        }
        vm.securityapps = SecurityApp.query(function(data) {
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityUser.app = data[0];
            }
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.$watch('vm.securityUser.app', function(n, o, e) {
            // check firstLoad tag
            if (!vm.securityUser.firstLoad) {
                vm.securityUser.roles = [];
                vm.securityUser.groups = [];
            }
            if (n) {
                vm.securityroles = SecurityRole.query({appId: n.id});
                vm.securitygroups = SecurityGroup.query({appId: n.id});
                // change firstLoad tag to false
                vm.securityUser.firstLoad = false;
            } else {
                vm.securityroles = [];
                vm.securitygroups = [];
            }
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityUserUpdate', result);
            $state.go('security-user', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityUser.passwordHash && (vm.securityUser.passwordHash !== vm.securityUser.confirmpassword)) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                if (vm.securityUser.id !== null) {
                    SecurityUser.update(vm.securityUser, onSaveSuccess, onSaveError);
                } else {
                    SecurityUser.save(vm.securityUser, onSaveSuccess, onSaveError);
                }
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
