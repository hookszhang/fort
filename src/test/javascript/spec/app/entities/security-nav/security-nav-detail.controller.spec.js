'use strict';

describe('Controller Tests', function() {

    describe('SecurityNav Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSecurityNav, MockSecurityResourceEntity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSecurityNav = jasmine.createSpy('MockSecurityNav');
            MockSecurityResourceEntity = jasmine.createSpy('MockSecurityResourceEntity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SecurityNav': MockSecurityNav,
                'SecurityResourceEntity': MockSecurityResourceEntity
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
