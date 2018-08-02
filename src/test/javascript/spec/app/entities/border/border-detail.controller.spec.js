'use strict';

describe('Controller Tests', function() {

    describe('Border Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBorder, MockPoints, MockNotification;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBorder = jasmine.createSpy('MockBorder');
            MockPoints = jasmine.createSpy('MockPoints');
            MockNotification = jasmine.createSpy('MockNotification');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Border': MockBorder,
                'Points': MockPoints,
                'Notification': MockNotification
            };
            createController = function() {
                $injector.get('$controller')("BorderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'civilDefanceApp:borderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
