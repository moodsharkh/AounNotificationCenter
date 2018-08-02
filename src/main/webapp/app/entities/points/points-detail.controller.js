(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('PointsDetailController', PointsDetailController);

    PointsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Points', 'Border'];

    function PointsDetailController($scope, $rootScope, $stateParams, previousState, entity, Points, Border) {
        var vm = this;

        vm.points = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:pointsUpdate', function(event, result) {
            vm.points = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
