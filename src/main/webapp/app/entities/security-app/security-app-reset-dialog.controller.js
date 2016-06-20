(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityAppResetController',SecurityAppResetController);

    SecurityAppResetController.$inject = ['$uibModalInstance', 'entity', 'SecurityAppResetAppSecret'];

    function SecurityAppResetController($uibModalInstance, entity, SecurityAppResetAppSecret) {
        var vm = this;
        vm.securityApp = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.resetAppSecret = function (id) {
            SecurityAppResetAppSecret.update({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
