'use strict';

describe('Controller Tests', function() {

    describe('SecurityNav Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityNav, MockSecurityResourceEntity, MockSecurityApp;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityNav = jasmine.createSpy('MockSecurityNav');
            MockSecurityResourceEntity = jasmine.createSpy('MockSecurityResourceEntity');
            MockSecurityApp = jasmine.createSpy('MockSecurityApp');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityNav': MockSecurityNav,
                'SecurityResourceEntity': MockSecurityResourceEntity,
                'SecurityApp': MockSecurityApp
            };
            createController = function() {
                $injector.get('$controller')("SecurityNavDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fortApp:securityNavUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
