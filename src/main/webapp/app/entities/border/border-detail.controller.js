(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('BorderDetailController', BorderDetailController);

    BorderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Border', 'Points', 'Notification'];

    function BorderDetailController($scope, $rootScope, $stateParams, previousState, entity, Border, Points, Notification) {
        var vm = this;

        vm.border = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:borderUpdate', function(event, result) {
            vm.border = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
