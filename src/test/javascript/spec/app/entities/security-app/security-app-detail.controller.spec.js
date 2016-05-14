'use strict';

describe('Controller Tests', function() {

    describe('SecurityApp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityApp;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityApp': MockSecurityApp
            };
            createController = function() {
                $injector.get('$controller')("SecurityAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityAppUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
