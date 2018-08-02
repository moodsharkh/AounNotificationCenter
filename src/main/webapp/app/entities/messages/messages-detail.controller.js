(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('MessagesDetailController', MessagesDetailController);

    MessagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Messages', 'Languages', 'Notification'];

    function MessagesDetailController($scope, $rootScope, $stateParams, previousState, entity, Messages, Languages, Notification) {
        var vm = this;

        vm.messages = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:messagesUpdate', function(event, result) {
            vm.messages = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
