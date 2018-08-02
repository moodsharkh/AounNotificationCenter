(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('MessagesDialogController', MessagesDialogController);

    MessagesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Messages', 'Languages', 'Notification'];

    function MessagesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Messages, Languages, Notification) {
        var vm = this;

        vm.messages = entity;
        vm.clear = clear;
        vm.save = save;
        vm.languages = Languages.query();
        vm.notifications = Notification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            console.log(JSON.stringify(vm.messages));


            if (vm.messages.id !== null) {
                Messages.update(vm.messages, onSaveSuccess, onSaveError);
            } else {
                Messages.save(vm.messages, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:messagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
