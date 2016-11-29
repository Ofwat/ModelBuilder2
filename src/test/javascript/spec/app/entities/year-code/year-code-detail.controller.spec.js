'use strict';

describe('Controller Tests', function() {

    describe('YearCode Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockYearCode, MockTransferBlockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockYearCode = jasmine.createSpy('MockYearCode');
            MockTransferBlockItem = jasmine.createSpy('MockTransferBlockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'YearCode': MockYearCode,
                'TransferBlockItem': MockTransferBlockItem
            };
            createController = function() {
                $injector.get('$controller')("YearCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:yearCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
