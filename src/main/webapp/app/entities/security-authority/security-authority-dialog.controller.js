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
        .controller('SecurityAuthorityDialogController', SecurityAuthorityDialogController);
    SecurityAuthorityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityAuthority', 'SecurityApp', 'SecurityResourceEntity', 'SecurityRole', 'Principal'];

    function SecurityAuthorityDialogController($timeout, $scope, $stateParams, $state, SecurityAuthority, SecurityApp, SecurityResourceEntity, SecurityRole, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityAuthority = SecurityAuthority.get({id : $stateParams.id});
        } else {
            vm.securityAuthority = {};
        }

        vm.securityapps = SecurityApp.query(function(data) {
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityAuthority.app = data[0];
            }
        });

        // init vm.securityAuthority.resources
        if (!vm.securityAuthority) {
            vm.securityAuthority = {
                resources : []
            }
        }

        $timeout(function() {
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function(result) {
            $scope.$emit('fortApp:securityAuthorityUpdate', result);
            $state.go('security-authority', null, { reload: true });
            vm.isSaving = false;
        };

        var onSaveError = function() {
            vm.isSaving = false;
        };

        // add watch app, search resource entity when app change
        $scope.$watch('vm.securityAuthority.app', function(n, o, e) {
            if (n) {
                vm.securityresourceentities = SecurityResourceEntity.query({appId: n.id});
            } else {
                vm.securityresourceentities = [];
            }

            if (vm.securityAuthority && !vm.securityAuthority.id) {
                vm.securityAuthority.resources = [];
            }
        });

        vm.save = function() {
            vm.isSaving = true;
            if (vm.securityAuthority.id !== null) {
                SecurityAuthority.update(vm.securityAuthority, onSaveSuccess, onSaveError);
            } else {
                SecurityAuthority.save(vm.securityAuthority, onSaveSuccess, onSaveError);
            }
        };
    }
})();
