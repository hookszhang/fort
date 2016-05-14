(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityRoleDeleteController',SecurityRoleDeleteController);

    SecurityRoleDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityRole'];

    function SecurityRoleDeleteController($uibModalInstance, entity, SecurityRole) {
        var vm = this;
        vm.securityRole = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityRole.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
