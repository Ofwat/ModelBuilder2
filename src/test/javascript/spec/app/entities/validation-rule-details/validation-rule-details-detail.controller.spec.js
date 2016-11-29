'use strict';

describe('Controller Tests', function() {

    describe('ValidationRuleDetails Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockValidationRuleDetails;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockValidationRuleDetails = jasmine.createSpy('MockValidationRuleDetails');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ValidationRuleDetails': MockValidationRuleDetails
            };
            createController = function() {
                $injector.get('$controller')("ValidationRuleDetailsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:validationRuleDetailsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
