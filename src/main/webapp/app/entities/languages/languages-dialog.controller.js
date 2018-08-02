(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('LanguagesDialogController', LanguagesDialogController);

    LanguagesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Languages', 'Messages'];

    function LanguagesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Languages, Messages) {
        var vm = this;

        vm.languages = entity;
        console.log(vm.languages);
        vm.clear = clear;
        vm.save = save;
        vm.messages = Messages.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.languages.id !== null) {
                Languages.update(vm.languages, onSaveSuccess, onSaveError);
            } else {
                Languages.save(vm.languages, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:languagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
