(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification', 'Border', 'Importance'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification, Border, Importance) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
