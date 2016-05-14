(function() {
    'use strict';

    angular
        .module('fortApp')
        .controller('SecurityGroupDeleteController',SecurityGroupDeleteController);

    SecurityGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'SecurityGroup'];

    function SecurityGroupDeleteController($uibModalInstance, entity, SecurityGroup) {
        var vm = this;
        vm.securityGroup = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SecurityGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
