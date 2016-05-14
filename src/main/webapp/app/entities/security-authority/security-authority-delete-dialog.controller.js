(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAuthorityDeleteController',SecurityAuthorityDeleteController);

    SecurityAuthorityDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityAuthority'];

    function SecurityAuthorityDeleteController($uibModalInstance, entity, SecurityAuthority) {
        var vm = this;
        vm.securityAuthority = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityAuthority.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
