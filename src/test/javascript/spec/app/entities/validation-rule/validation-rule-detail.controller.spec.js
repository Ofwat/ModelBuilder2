'use strict';

describe('Controller Tests', function() {

    describe('ValidationRule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockValidationRule, MockValidationRuleDetails, MockValidationRuleItem, MockModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockValidationRule = jasmine.createSpy('MockValidationRule');
            MockValidationRuleDetails = jasmine.createSpy('MockValidationRuleDetails');
            MockValidationRuleItem = jasmine.createSpy('MockValidationRuleItem');
            MockModel = jasmine.createSpy('MockModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ValidationRule': MockValidationRule,
                'ValidationRuleDetails': MockValidationRuleDetails,
                'ValidationRuleItem': MockValidationRuleItem,
                'Model': MockModel
            };
            createController = function() {
                $injector.get('$controller')("ValidationRuleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:validationRuleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
