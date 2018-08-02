(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('LanguagesController', LanguagesController);

    LanguagesController.$inject = ['Languages'];

    function LanguagesController(Languages) {

        var vm = this;

        vm.languages = [];

        loadAll();

        function loadAll() {
            Languages.query(function(result) {
                vm.languages = result;
                vm.searchQuery = null;
            });
        }
    }
})();
