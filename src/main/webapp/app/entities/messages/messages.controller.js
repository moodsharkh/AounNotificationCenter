(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('MessagesController', MessagesController);

    MessagesController.$inject = ['Messages'];

    function MessagesController(Messages) {

        var vm = this;

        vm.messages = [];

        loadAll();

        function loadAll() {
            Messages.query(function(result) {
                vm.messages = result;
                vm.searchQuery = null;
            });
        }
    }
})();
