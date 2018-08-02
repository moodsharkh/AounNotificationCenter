(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('PointsDialogController', PointsDialogController);

    PointsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Points', 'Border'];

    function PointsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Points, Border) {
        var vm = this;

        vm.points = entity;
        vm.clear = clear;
        vm.save = save;
        vm.borders = Border.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.points.id !== null) {
                Points.update(vm.points, onSaveSuccess, onSaveError);
            } else {
                Points.save(vm.points, onSaveSuccess, onSaveError);
            }
            console.log(JSON.stringify(vm.points) );
        }

        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:pointsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
