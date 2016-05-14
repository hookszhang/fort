'use strict';

describe('Controller Tests', function() {

    describe('SecurityUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityUser, MockSecurityApp, MockSecurityRole, MockSecurityGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityUser = jasmine.createSpy('MockSecurityUser');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            MockSecurityRole = jasmine.createSpy('MockSecurityRole');
            MockSecurityGroup = jasmine.createSpy('MockSecurityGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityUser': MockSecurityUser,
                'SecurityApp': MockSecurityApp,
                'SecurityRole': MockSecurityRole,
                'SecurityGroup': MockSecurityGroup
            };
            createController = function() {
                $injector.get('$controller')("SecurityUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
