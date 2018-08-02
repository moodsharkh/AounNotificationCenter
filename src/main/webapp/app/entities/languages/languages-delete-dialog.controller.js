(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('LanguagesDeleteController',LanguagesDeleteController);

    LanguagesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Languages'];

    function LanguagesDeleteController($uibModalInstance, entity, Languages) {
        var vm = this;

        vm.languages = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Languages.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
