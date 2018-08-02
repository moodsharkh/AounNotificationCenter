'use strict';

describe('Controller Tests', function() {

    describe('Messages Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMessages, MockLanguages, MockNotification;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMessages = jasmine.createSpy('MockMessages');
            MockLanguages = jasmine.createSpy('MockLanguages');
            MockNotification = jasmine.createSpy('MockNotification');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Messages': MockMessages,
                'Languages': MockLanguages,
                'Notification': MockNotification
            };
            createController = function() {
                $injector.get('$controller')("MessagesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'civilDefanceApp:messagesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
