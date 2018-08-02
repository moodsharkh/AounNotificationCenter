(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('ImportanceDetailController', ImportanceDetailController);

    ImportanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Importance', 'Notification'];

    function ImportanceDetailController($scope, $rootScope, $stateParams, previousState, entity, Importance, Notification) {
        var vm = this;

        vm.importance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:importanceUpdate', function(event, result) {
            vm.importance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
