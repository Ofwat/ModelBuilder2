'use strict';

describe('Controller Tests', function() {

    describe('Text Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockText, MockTextBlock, MockModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockText = jasmine.createSpy('MockText');
            MockTextBlock = jasmine.createSpy('MockTextBlock');
            MockModel = jasmine.createSpy('MockModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Text': MockText,
                'TextBlock': MockTextBlock,
                'Model': MockModel
            };
            createController = function() {
                $injector.get('$controller')("TextDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:textUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
