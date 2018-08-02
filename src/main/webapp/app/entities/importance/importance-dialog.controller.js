(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('ImportanceDialogController', ImportanceDialogController);

    ImportanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Importance', 'Notification'];

    function ImportanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Importance, Notification) {
        var vm = this;

        vm.importance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.notifications = Notification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.importance.id !== null) {
                Importance.update(vm.importance, onSaveSuccess, onSaveError);
            } else {
                Importance.save(vm.importance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:importanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
