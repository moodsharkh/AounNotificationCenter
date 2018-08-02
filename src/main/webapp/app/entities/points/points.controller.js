(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('PointsController', PointsController);

    PointsController.$inject = ['Points'];

    function PointsController(Points) {

        var vm = this;

        vm.points = [];

        loadAll();

        function loadAll() {
            Points.query(function(result) {
                vm.points = result;
                vm.searchQuery = null;
            });
        }
    }
})();
