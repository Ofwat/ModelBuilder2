'use strict';

describe('Controller Tests', function() {

    describe('ValidationRuleItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockValidationRuleItem, MockValidationRule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockValidationRuleItem = jasmine.createSpy('MockValidationRuleItem');
            MockValidationRule = jasmine.createSpy('MockValidationRule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ValidationRuleItem': MockValidationRuleItem,
                'ValidationRule': MockValidationRule
            };
            createController = function() {
                $injector.get('$controller')("ValidationRuleItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:validationRuleItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
