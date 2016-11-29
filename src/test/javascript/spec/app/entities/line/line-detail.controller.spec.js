'use strict';

describe('Controller Tests', function() {

    describe('Line Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLine, MockLineDetails, MockCellRange, MockCell, MockModelBuilderDocument, MockSection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLine = jasmine.createSpy('MockLine');
            MockLineDetails = jasmine.createSpy('MockLineDetails');
            MockCellRange = jasmine.createSpy('MockCellRange');
            MockCell = jasmine.createSpy('MockCell');
            MockModelBuilderDocument = jasmine.createSpy('MockModelBuilderDocument');
            MockSection = jasmine.createSpy('MockSection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Line': MockLine,
                'LineDetails': MockLineDetails,
                'CellRange': MockCellRange,
                'Cell': MockCell,
                'ModelBuilderDocument': MockModelBuilderDocument,
                'Section': MockSection
            };
            createController = function() {
                $injector.get('$controller')("LineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:lineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
