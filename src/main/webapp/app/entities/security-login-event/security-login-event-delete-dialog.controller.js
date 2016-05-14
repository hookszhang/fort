(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityLoginEventDeleteController',SecurityLoginEventDeleteController);

    SecurityLoginEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityLoginEvent'];

    function SecurityLoginEventDeleteController($uibModalInstance, entity, SecurityLoginEvent) {
        var vm = this;
        vm.securityLoginEvent = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityLoginEvent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
