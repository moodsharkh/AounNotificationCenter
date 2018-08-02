(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('LanguagesDetailController', LanguagesDetailController);

    LanguagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Languages', 'Messages'];

    function LanguagesDetailController($scope, $rootScope, $stateParams, previousState, entity, Languages, Messages) {
        var vm = this;

        vm.languages = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('civilDefanceApp:languagesUpdate', function(event, result) {
            vm.languages = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
