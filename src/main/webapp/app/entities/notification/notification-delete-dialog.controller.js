(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('NotificationDeleteController',NotificationDeleteController);

    NotificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Notification','Border'];

    function NotificationDeleteController($uibModalInstance, entity, Notification,Border) {
        var vm = this;

        vm.notification = entity;
        if (vm.notification.title.substring(vm.notification.title.length-2)=="##")
            vm.notification.title=   vm.notification.title.substring(0,vm.notification.title.length-2);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        vm.borders = [];
        loadAll();

        vm.border;
        function loadAll() {
            Border.query(function(result) {
                vm.borders = result;
                vm.searchQuery = null;
                for (var i=0; i<vm.borders.length;i++){
                    if (vm.borders[i].id == vm.notification.notimanytoone.id)
                        vm.border = vm.borders[i];

                }
            });
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            vm.border.colorReset = 1;
            Border.update(vm.border, onSaveSuccess, onSaveError);
            Notification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:borderUpdate', result);
            // $uibModalInstance.close(result);
            // vm.isSaving = false;

        }
        function onSaveError () {
            // vm.isSaving = false;
        }
    }
})();
