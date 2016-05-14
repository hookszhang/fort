(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityNavDeleteController',SecurityNavDeleteController);

    SecurityNavDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityNav'];

    function SecurityNavDeleteController($uibModalInstance, entity, SecurityNav) {
        var vm = this;
        vm.securityNav = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityNav.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
