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
        .controller('SecurityNavDialogController', SecurityNavDialogController);

    SecurityNavDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'SecurityNav', 'SecurityResourceEntity', 'SecurityApp', 'Principal'];

    function SecurityNavDialogController ($timeout, $scope, $stateParams, $state, SecurityNav, SecurityResourceEntity, SecurityApp, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityNav = SecurityNav.get({id : $stateParams.id});
        } else {
            vm.securityNav = {};
        }

        vm.securityapps = SecurityApp.query(function(data) {
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityNav.app = data[0];
            }
        });

        $scope.$watch('vm.securityNav.app', function(n, o, e) {
            vm.securitynavs = SecurityNav.query({app: n}, function() {
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
            vm.securityresourceentities = SecurityResourceEntity.query({app: n});
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('fortApp:securityNavUpdate', result);
            $state.go('security-nav', null, { reload: true });
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
