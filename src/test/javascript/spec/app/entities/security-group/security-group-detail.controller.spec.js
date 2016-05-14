'use strict';

describe('Controller Tests', function() {

    describe('SecurityGroup Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityGroup, MockSecurityApp, MockSecurityUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityGroup = jasmine.createSpy('MockSecurityGroup');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            MockSecurityUser = jasmine.createSpy('MockSecurityUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityGroup': MockSecurityGroup,
                'SecurityApp': MockSecurityApp,
                'SecurityUser': MockSecurityUser
            };
            createController = function() {
                $injector.get('$controller')("SecurityGroupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityGroupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
