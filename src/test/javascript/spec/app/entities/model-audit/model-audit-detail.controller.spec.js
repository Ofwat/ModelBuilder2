'use strict';

describe('Controller Tests', function() {

    describe('ModelAudit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModelAudit, MockAuditDetails, MockAuditChange, MockModel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModelAudit = jasmine.createSpy('MockModelAudit');
            MockAuditDetails = jasmine.createSpy('MockAuditDetails');
            MockAuditChange = jasmine.createSpy('MockAuditChange');
            MockModel = jasmine.createSpy('MockModel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ModelAudit': MockModelAudit,
                'AuditDetails': MockAuditDetails,
                'AuditChange': MockAuditChange,
                'Model': MockModel
            };
            createController = function() {
                $injector.get('$controller')("ModelAuditDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'modelBuilderApp:modelAuditUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
