'use strict';

angular.module('thesisplatformApp')
    .controller('ReadoutController', function ($scope, Readout, ParseLinks) {
    //
                $scope.labels = [];
                $scope.colours =  ['#2f5f77',
                                   '#2f5f77',
                                   '#2f5f77',
                                   '#2f5f77'
                                                 ];
                $scope.series = ['Readouts from choosen sensor'];
                $scope.data = [[]];
                $scope.onClick = function (points, evt) {
                console.log(points, evt);
                };
                //
        $scope.readouts = [];
        $scope.readoutsForAgent = [];
        $scope.agents = [];
        $scope.sensors = [];
        $scope.page = 0;
        $scope.s;
        var dateFormat = 'yyyy-MM-dd HH:mm';
        var today = new Date();
               $scope.fromDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
               $scope.toDate = new Date(today.getFullYear(), today.getMonth()+1, today.getDate());
        $scope.choosenAgent;
        $scope.selectedAgentName = "";
        $scope.selectedSensorName = "";
        $scope.loadAll = function() {
            Readout.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.readouts = result;
            });
        };

        $scope.loadReadoutsForAgent = function() {
                    Readout.readouts_for_agent({agent : $scope.choosenAgent, fromDate : $scope.fromDate, toDate: $scope.toDate, page: $scope.page, size: 20}, function(result, headers) {
                        $scope.links = ParseLinks.parse(headers('link'));
                        $scope.readoutsForAgent = result;
                    });
                };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
            $scope.loadLabels();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Readout.get({id: id}, function(result) {
                $scope.readout = result;
                $('#deleteReadoutConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Readout.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteReadoutConfirmation').modal('hide');
                    $scope.clear();
                });
        };


        $scope.refresh = function () {
            $scope.loadAll();
             $scope.loadReadoutsWithTimeRange();
             $scope.loadLabelsWithTimeRange();
            $scope.clear();
        };

         $scope.setCurrentSensor = function (sensor, sensorName) {
                    $scope.s=sensor;
                    $scope.selectedSensorName=sensorName;
                };

          $scope.setCurrentAgent = function (agent, agentName) {
                            $scope.choosenAgent=agent;
                            $scope.selectedAgentName=agentName;
                        };

        $scope.clear = function () {
            $scope.readout = {
                readoutValue: null,
                readoutTime: null,
                id: null
            };
        };

         $scope.loadLabels = function(){
                    console.log(1, $scope.labels);
                 Readout.labels({s: $scope.s}, function(result){
                    $scope.labels = result;
                    console.log(1, $scope.labels);
                 });
                };
          $scope.loadReadouts = function(){
                             console.log(777, $scope.s);
                          Readout.readouts({s: $scope.s}, function(result){
                             $scope.data[0] = result;
                             console.log(1, $scope.readouts);
                          });
                         };
          $scope.loadAgents = function(){
                                   Readout.agents({page: $scope.page, size: 20}, function(result){
                                      $scope.agents = result;
                                      console.log(1, $scope.agents);
                                   });
                                  };
           $scope.loadSensors = function(){
                                             Readout.sensors({a: $scope.choosenAgent}, function(result){
                                                $scope.sensors = result;
                                                console.log(222, $scope.choosenAgent);
                                             });
                                            };
          $scope.loadReadoutsWithTimeRange = function(){
                                                        Readout.readoutswithtime({ fromDate: $scope.fromDate, toDate: $scope.toDate, sensor: $scope.s}, function(result){
                                                               $scope.data[0] = result;
                                                               console.log(2223, $scope.fromDate);
                                                        });
                                                       };

          $scope.drawChart = function(){
          if($scope.choosenAgent == null || $scope.s == null){
                       $('#NoAgentOrSensorChoosenAlert').modal('show');
                       }
               else if($scope.data[0] == 0){
             $('#NoReadoutsAlert').modal('show');
                }

            else{
            $scope.loadReadoutsWithTimeRange();
            $scope.loadLabelsWithTimeRange();
            $scope.refresh();
            }
          };

          $scope.loadLabelsWithTimeRange = function(){
                                                                  Readout.labelswithtime({fromDate: $scope.fromDate, toDate: $scope.toDate, sensor: $scope.s}, function(result){
                                                                   $scope.labels = result;
                                                                     console.log(2223, $scope.fromDate);
                                                                  });
                                                                 };

         $scope.loadLabelsWithTimeRange();
          $scope.loadReadoutsWithTimeRange();
          $scope.loadAgents();
           $scope.loadSensors();
    });
