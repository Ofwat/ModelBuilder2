'use strict';

describe('Controller Tests', function() {

    describe('FormHeadingCell Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFormHeadingCell, MockForm;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFormHeadingCell = jasmine.createSpy('MockFormHeadingCell');
            MockForm = jasmine.createSpy('MockForm');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FormHeadingCell': MockFormHeadingCell,
                'Form': MockForm
            };
            createController = function() {
                $injector.get('$controller')("FormHeadingCellDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:formHeadingCellUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
