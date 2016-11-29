'use strict';

describe('Controller Tests', function() {

    describe('Transfer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTransfer, MockTransferCondition, MockTransferBlock, MockModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTransfer = jasmine.createSpy('MockTransfer');
            MockTransferCondition = jasmine.createSpy('MockTransferCondition');
            MockTransferBlock = jasmine.createSpy('MockTransferBlock');
            MockModel = jasmine.createSpy('MockModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Transfer': MockTransfer,
                'TransferCondition': MockTransferCondition,
                'TransferBlock': MockTransferBlock,
                'Model': MockModel
            };
            createController = function() {
                $injector.get('$controller')("TransferDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:transferUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
