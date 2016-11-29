'use strict';

describe('Controller Tests', function() {

    describe('Section Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSection, MockSectionDetails, MockModelBuilderDocument, MockLine, MockForm, MockPage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSection = jasmine.createSpy('MockSection');
            MockSectionDetails = jasmine.createSpy('MockSectionDetails');
            MockModelBuilderDocument = jasmine.createSpy('MockModelBuilderDocument');
            MockLine = jasmine.createSpy('MockLine');
            MockForm = jasmine.createSpy('MockForm');
            MockPage = jasmine.createSpy('MockPage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Section': MockSection,
                'SectionDetails': MockSectionDetails,
                'ModelBuilderDocument': MockModelBuilderDocument,
                'Line': MockLine,
                'Form': MockForm,
                'Page': MockPage
            };
            createController = function() {
                $injector.get('$controller')("SectionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:sectionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
