'use strict';

describe('Controller Tests', function() {

    describe('Form Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockForm, MockFormDetails, MockFormCell, MockFormHeadingCell, MockSection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockForm = jasmine.createSpy('MockForm');
            MockFormDetails = jasmine.createSpy('MockFormDetails');
            MockFormCell = jasmine.createSpy('MockFormCell');
            MockFormHeadingCell = jasmine.createSpy('MockFormHeadingCell');
            MockSection = jasmine.createSpy('MockSection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Form': MockForm,
                'FormDetails': MockFormDetails,
                'FormCell': MockFormCell,
                'FormHeadingCell': MockFormHeadingCell,
                'Section': MockSection
            };
            createController = function() {
                $injector.get('$controller')("FormDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:formUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
