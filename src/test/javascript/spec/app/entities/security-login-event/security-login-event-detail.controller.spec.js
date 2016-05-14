'use strict';

describe('Controller Tests', function() {

    describe('SecurityLoginEvent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityLoginEvent, MockSecurityUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityLoginEvent = jasmine.createSpy('MockSecurityLoginEvent');
            MockSecurityUser = jasmine.createSpy('MockSecurityUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityLoginEvent': MockSecurityLoginEvent,
                'SecurityUser': MockSecurityUser
            };
            createController = function() {
                $injector.get('$controller')("SecurityLoginEventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityLoginEventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
