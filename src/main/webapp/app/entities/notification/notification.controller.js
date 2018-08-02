(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['Notification'];

    function NotificationController(Notification) {

        var vm = this;

        vm.notifications = [];

        loadAll();

        function loadAll() {
            Notification.query(function(result) {
                vm.notifications = result;
                vm.searchQuery = null;
                for (var i = 0; i<vm.notifications.length;i++){
                    if (vm.notifications[i].title.substring(vm.notifications[i].title.length-2)=="##")
                        vm.notifications[i].title=   vm.notifications[i].title.substring(0,vm.notifications[i].title.length-2);
                }
            });
        }

    }
})();
