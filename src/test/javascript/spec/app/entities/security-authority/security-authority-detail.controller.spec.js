'use strict';

describe('Controller Tests', function() {

    describe('SecurityAuthority Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityAuthority, MockSecurityApp, MockSecurityResourceEntity, MockSecurityRole;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityAuthority = jasmine.createSpy('MockSecurityAuthority');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            MockSecurityResourceEntity = jasmine.createSpy('MockSecurityResourceEntity');
            MockSecurityRole = jasmine.createSpy('MockSecurityRole');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityAuthority': MockSecurityAuthority,
                'SecurityApp': MockSecurityApp,
                'SecurityResourceEntity': MockSecurityResourceEntity,
                'SecurityRole': MockSecurityRole
            };
            createController = function() {
                $injector.get('$controller')("SecurityAuthorityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityAuthorityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
