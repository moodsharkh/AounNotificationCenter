(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('ImportanceDeleteController',ImportanceDeleteController);

    ImportanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Importance'];

    function ImportanceDeleteController($uibModalInstance, entity, Importance) {
        var vm = this;

        vm.importance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Importance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
