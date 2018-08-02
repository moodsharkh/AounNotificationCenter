(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('BorderDeleteController',BorderDeleteController);

    BorderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Border','Points','Notification'];

    function BorderDeleteController($uibModalInstance, entity, Border,Points,Notification) {
        var vm = this;

        vm.border = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        vm.points = [];
        var tempList = [];

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {


            Border.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
