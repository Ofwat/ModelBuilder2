'use strict';

describe('Controller Tests', function() {

    describe('Model Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModel, MockModelDetails, MockModelAudit, MockItem, MockYear, MockInput, MockHeading, MockValidationRule, MockCompanyPage, MockModelBuilderDocument, MockPage, MockTransfer, MockMacro, MockText;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModel = jasmine.createSpy('MockModel');
            MockModelDetails = jasmine.createSpy('MockModelDetails');
            MockModelAudit = jasmine.createSpy('MockModelAudit');
            MockItem = jasmine.createSpy('MockItem');
            MockYear = jasmine.createSpy('MockYear');
            MockInput = jasmine.createSpy('MockInput');
            MockHeading = jasmine.createSpy('MockHeading');
            MockValidationRule = jasmine.createSpy('MockValidationRule');
            MockCompanyPage = jasmine.createSpy('MockCompanyPage');
            MockModelBuilderDocument = jasmine.createSpy('MockModelBuilderDocument');
            MockPage = jasmine.createSpy('MockPage');
            MockTransfer = jasmine.createSpy('MockTransfer');
            MockMacro = jasmine.createSpy('MockMacro');
            MockText = jasmine.createSpy('MockText');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Model': MockModel,
                'ModelDetails': MockModelDetails,
                'ModelAudit': MockModelAudit,
                'Item': MockItem,
                'Year': MockYear,
                'Input': MockInput,
                'Heading': MockHeading,
                'ValidationRule': MockValidationRule,
                'CompanyPage': MockCompanyPage,
                'ModelBuilderDocument': MockModelBuilderDocument,
                'Page': MockPage,
                'Transfer': MockTransfer,
                'Macro': MockMacro,
                'Text': MockText
            };
            createController = function() {
                $injector.get('$controller')("ModelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:modelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
