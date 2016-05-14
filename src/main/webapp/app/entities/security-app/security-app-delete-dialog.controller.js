(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAppDeleteController',SecurityAppDeleteController);

    SecurityAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityApp'];

    function SecurityAppDeleteController($uibModalInstance, entity, SecurityApp) {
        var vm = this;
        vm.securityApp = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityApp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
