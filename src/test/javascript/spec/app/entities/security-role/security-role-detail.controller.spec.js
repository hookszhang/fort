'use strict';

describe('Controller Tests', function() {

    describe('SecurityRole Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityRole, MockSecurityApp, MockSecurityAuthority, MockSecurityUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityRole = jasmine.createSpy('MockSecurityRole');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            MockSecurityAuthority = jasmine.createSpy('MockSecurityAuthority');
            MockSecurityUser = jasmine.createSpy('MockSecurityUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityRole': MockSecurityRole,
                'SecurityApp': MockSecurityApp,
                'SecurityAuthority': MockSecurityAuthority,
                'SecurityUser': MockSecurityUser
            };
            createController = function() {
                $injector.get('$controller')("SecurityRoleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityRoleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
