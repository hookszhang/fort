(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityUserDeleteController',SecurityUserDeleteController);

    SecurityUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityUser'];

    function SecurityUserDeleteController($uibModalInstance, entity, SecurityUser) {
        var vm = this;
        vm.securityUser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
