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
        .controller('SecurityGroupDialogController', SecurityGroupDialogController);

    SecurityGroupDialogController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'SecurityGroup', 'SecurityApp', 'SecurityUser', 'Principal'];

    function SecurityGroupDialogController ($timeout, $scope, $state, $stateParams, SecurityGroup, SecurityApp, SecurityUser, Principal) {
        var vm = this;
        if ($stateParams.id) {
            vm.securityGroup = SecurityGroup.get({id : $stateParams.id});
        } else {
            vm.securityGroup = {};
        }

        vm.securityapps = SecurityApp.query(function(data) {
            if (!$stateParams.id && Principal.hasAnyAuthority(['ROLE_SECURITY_APP'])) {
                vm.securityGroup.app = data[0];
            }
        });

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
