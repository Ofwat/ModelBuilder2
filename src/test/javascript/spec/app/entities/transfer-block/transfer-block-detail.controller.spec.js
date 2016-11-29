'use strict';

describe('Controller Tests', function() {

    describe('TransferBlock Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTransferBlock, MockTransferBlockDetails, MockTransferBlockItem, MockTransfer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTransferBlock = jasmine.createSpy('MockTransferBlock');
            MockTransferBlockDetails = jasmine.createSpy('MockTransferBlockDetails');
            MockTransferBlockItem = jasmine.createSpy('MockTransferBlockItem');
            MockTransfer = jasmine.createSpy('MockTransfer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TransferBlock': MockTransferBlock,
                'TransferBlockDetails': MockTransferBlockDetails,
                'TransferBlockItem': MockTransferBlockItem,
                'Transfer': MockTransfer
            };
            createController = function() {
                $injector.get('$controller')("TransferBlockDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:transferBlockUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
