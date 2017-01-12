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
        .controller('SecurityRoleDialogController', SecurityRoleDialogController);

    SecurityRoleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityRole', 'SecurityApp', 'SecurityAuthority', 'SecurityUser', 'Principal'];

    function SecurityRoleDialogController ($timeout, $scope, $stateParams, $state, SecurityRole, SecurityApp, SecurityAuthority, SecurityUser, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityRole = SecurityRole.get({id : $stateParams.id}, function(){
                // add firstLoad tag
                vm.securityRole.firstLoad = true;
            });
        } else {
            vm.securityRole = {};
        }
        vm.securityapps = SecurityApp.query(function(data) {
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityRole.app = data[0];
            }
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.$watch('vm.securityRole.app', function(n, o, e) {
            if (!vm.securityRole.firstLoad) {
                vm.securityRole.authorities = [];
            }
            if (n) {
                vm.securityauthorities = SecurityAuthority.query({appId: n.id});
                // change firstLoad tag to false
                vm.securityRole.firstLoad = false;
            }
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityRoleUpdate', result);
            $state.go('security-role', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.securityRole.id !== null) {
                SecurityRole.update(vm.securityRole, onSaveSuccess, onSaveError);
            } else {
                SecurityRole.save(vm.securityRole, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
