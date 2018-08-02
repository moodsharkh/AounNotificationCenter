(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('ImportanceController', ImportanceController);

    ImportanceController.$inject = ['Importance'];

    function ImportanceController(Importance) {

        var vm = this;

        vm.importances = [];

        loadAll();

        function loadAll() {
            Importance.query(function(result) {
                vm.importances = result;
                vm.searchQuery = null;
            });
        }
    }
})();
