'use strict';

describe('Controller Tests', function() {

    describe('ModelBuilderDocument Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModelBuilderDocument, MockModel, MockPage, MockSection, MockLine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModelBuilderDocument = jasmine.createSpy('MockModelBuilderDocument');
            MockModel = jasmine.createSpy('MockModel');
            MockPage = jasmine.createSpy('MockPage');
            MockSection = jasmine.createSpy('MockSection');
            MockLine = jasmine.createSpy('MockLine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ModelBuilderDocument': MockModelBuilderDocument,
                'Model': MockModel,
                'Page': MockPage,
                'Section': MockSection,
                'Line': MockLine
            };
            createController = function() {
                $injector.get('$controller')("ModelBuilderDocumentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:modelBuilderDocumentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
