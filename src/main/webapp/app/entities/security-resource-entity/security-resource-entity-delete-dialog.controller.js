(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityResourceEntityDeleteController',SecurityResourceEntityDeleteController);

    SecurityResourceEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityResourceEntity'];

    function SecurityResourceEntityDeleteController($uibModalInstance, entity, SecurityResourceEntity) {
        var vm = this;
        vm.securityResourceEntity = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityResourceEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
