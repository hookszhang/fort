'use strict';

describe('Controller Tests', function() {

    describe('SecurityResourceEntity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityResourceEntity, MockSecurityApp, MockSecurityAuthority;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityResourceEntity = jasmine.createSpy('MockSecurityResourceEntity');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            MockSecurityAuthority = jasmine.createSpy('MockSecurityAuthority');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityResourceEntity': MockSecurityResourceEntity,
                'SecurityApp': MockSecurityApp,
                'SecurityAuthority': MockSecurityAuthority
            };
            createController = function() {
                $injector.get('$controller')("SecurityResourceEntityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityResourceEntityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
