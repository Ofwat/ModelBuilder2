'use strict';

describe('Controller Tests', function() {

    describe('Page Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPage, MockPageDetails, MockSection, MockModelBuilderDocument, MockModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPage = jasmine.createSpy('MockPage');
            MockPageDetails = jasmine.createSpy('MockPageDetails');
            MockSection = jasmine.createSpy('MockSection');
            MockModelBuilderDocument = jasmine.createSpy('MockModelBuilderDocument');
            MockModel = jasmine.createSpy('MockModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Page': MockPage,
                'PageDetails': MockPageDetails,
                'Section': MockSection,
                'ModelBuilderDocument': MockModelBuilderDocument,
                'Model': MockModel
            };
            createController = function() {
                $injector.get('$controller')("PageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:pageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
