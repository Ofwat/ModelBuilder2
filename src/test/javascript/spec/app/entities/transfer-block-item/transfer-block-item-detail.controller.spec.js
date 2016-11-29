'use strict';

describe('Controller Tests', function() {

    describe('TransferBlockItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTransferBlockItem, MockYearCode, MockTransferBlock;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTransferBlockItem = jasmine.createSpy('MockTransferBlockItem');
            MockYearCode = jasmine.createSpy('MockYearCode');
            MockTransferBlock = jasmine.createSpy('MockTransferBlock');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TransferBlockItem': MockTransferBlockItem,
                'YearCode': MockYearCode,
                'TransferBlock': MockTransferBlock
            };
            createController = function() {
                $injector.get('$controller')("TransferBlockItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:transferBlockItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
