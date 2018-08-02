(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('BorderController', BorderController);

    BorderController.$inject = ['Border'];

    function BorderController(Border) {

        var vm = this;

        vm.borders = [];

        loadAll();

        function loadAll() {
            Border.query(function(result) {
                vm.borders = result;
                vm.searchQuery = null;
            });
        }
    }
})();
