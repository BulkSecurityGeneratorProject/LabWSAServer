'use strict';

angular.module('thesisplatformApp')
    .controller('ReadoutChartsController', function ($scope, Readout, ParseLinks) {
    //
                $scope.labels = [];
                $scope.labels2 = [];
                $scope.labels3 = [];
                $scope.labels4 = [];
                $scope.colours =  ['#2f5f77',
                                   '#2f5f77',
                                   '#2f5f77',
                                   '#2f5f77'
                                                 ];
                $scope.series = ['Readouts from choosen sensor'];
                $scope.data = [[]];
                $scope.data2 = [[]];
                $scope.data3 = [[]];
                $scope.data4 = [[]];
                $scope.onClick = function (points, evt) {
                console.log(points, evt);
                };
                //
        $scope.readouts = [];
        $scope.agents = [];
        $scope.sensors = [];
        $scope.sensors2 = [];
        $scope.sensors3 = [];
        $scope.sensors4 = [];
        $scope.page = 0;
        $scope.s;
        $scope.sensor2;
        $scope.sensor3;
        $scope.sensor4;
        var dateFormat = 'yyyy-MM-dd HH:mm';
        var today = new Date();
        $scope.fromDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        $scope.toDate = new Date(today.getFullYear(), today.getMonth()+1, today.getDate());
        $scope.fromDate2 = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        $scope.toDate2 = new Date(today.getFullYear(), today.getMonth()+1, today.getDate());
        $scope.fromDate3 = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        $scope.toDate3 = new Date(today.getFullYear(), today.getMonth()+1, today.getDate());
        $scope.fromDate4 = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        $scope.toDate4 = new Date(today.getFullYear(), today.getMonth()+1, today.getDate());
        $scope.choosenAgent;
        $scope.choosenAgent2;
        $scope.choosenAgent3;
        $scope.choosenAgent4;
        $scope.chart1Id = 1;
        $scope.chart2Id = 2;
        $scope.chart3Id = 3;
        $scope.chart4Id = 4;
        $scope.selectedAgentName = "";
        $scope.selectedAgentName2 = "";
        $scope.selectedAgentName3 = "";
        $scope.selectedAgentName4 = "";
        $scope.selectedSensorName = "";
        $scope.selectedSensorName2 = "";
        $scope.selectedSensorName3 = "";
        $scope.selectedSensorName4 = "";
        $scope.loadAll = function() {
            Readout.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.readouts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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


        $scope.refresh = function (chartId) {
            $scope.loadAll();
             $scope.loadReadoutsWithTimeRange(chartId);
             $scope.loadLabelsWithTimeRange(chartId);
            $scope.clear();
        };

         $scope.setCurrentSensor = function (chartId, sensor, sensorName) {
             switch(chartId){
                              case 1:
                                 $scope.s=sensor;
                                 $scope.selectedSensorName=sensorName;
                                break;
                              case 2:
                                  $scope.sensor2=sensor;
                                  $scope.selectedSensorName2=sensorName;
                                  break;
                              case 3:
                                  $scope.sensor3=sensor;
                                  $scope.selectedSensorName3=sensorName;
                                  break;
                              case 4:
                                  $scope.sensor4=sensor;
                                  $scope.selectedSensorName4=sensorName;
                                  break;
                          }
         };

          $scope.setCurrentAgent = function (chartId, agent, agentName) {
              switch(chartId){
                  case 1:
                     $scope.choosenAgent=agent;
                     $scope.selectedAgentName=agentName;
                    break;
                  case 2:
                      $scope.choosenAgent2=agent;
                      $scope.selectedAgentName2=agentName;
                      break;
                  case 3:
                      $scope.choosenAgent3=agent;
                      $scope.selectedAgentName3=agentName;
                      break;
                  case 4:
                      $scope.choosenAgent4=agent;
                      $scope.selectedAgentName4=agentName;
                      break;
              }
          };

        $scope.clear = function () {
            $scope.readout = {
                readoutValue: null,
                readoutTime: null,
                id: null
            };
        };

          $scope.loadAgents = function(){
                                   Readout.agents({page: $scope.page, size: 20}, function(result){
                                      $scope.agents = result;
                                      console.log(1, $scope.agents);
                                   });
                                  };
           $scope.loadSensors = function(chartId){

            switch(chartId){
            case 1:
             Readout.sensors({a:  $scope.choosenAgent}, function(result){
            $scope.sensors = result;
             console.log(222, $scope.choosenAgent);
           });
             break;
            case 2:
             Readout.sensors({a:  $scope.choosenAgent2}, function(result){
            $scope.sensors2 = result;
            });
             break;
             case 3:
             Readout.sensors({a:  $scope.choosenAgent3}, function(result){
            $scope.sensors3 = result;
           });
             break;
             case 4:
             Readout.sensors({a:  $scope.choosenAgent4}, function(result){
            $scope.sensors4 = result;
           });
              break;
            }
           };
          $scope.loadReadoutsWithTimeRange = function(chartId){
                                                        switch(chartId){
                                                                          case 1:
                                                        Readout.readoutswithtime({ fromDate: $scope.fromDate, toDate: $scope.toDate, sensor: $scope.s}, function(result){
                                                        $scope.data[0] = result;
                                                        });
                                                                            break;
                                                                          case 2:
                                                        Readout.readoutswithtime({ fromDate: $scope.fromDate2, toDate: $scope.toDate2, sensor: $scope.sensor2}, function(result){
                                                        $scope.data2[0] = result;
                                                        });
                                                                              break;
                                                                          case 3:
                                                        Readout.readoutswithtime({ fromDate: $scope.fromDate3, toDate: $scope.toDate3, sensor: $scope.sensor3}, function(result){
                                                        $scope.data3[0] = result;
                                                        });
                                                                              break;
                                                                          case 4:
                                                        Readout.readoutswithtime({ fromDate: $scope.fromDate4, toDate: $scope.toDate4, sensor: $scope.sensor4}, function(result){
                                                        $scope.data4[0] = result;
                                                        });
                                                                              break;
                                                      }
         };

          $scope.drawChart = function(chartId){
            $scope.loadReadoutsWithTimeRange(chartId);
            $scope.loadLabelsWithTimeRange(chartId);
            $scope.refresh(chartId);
       /*    }*/
          };

          $scope.loadLabelsWithTimeRange = function(chartId){
                                                        switch(chartId){
                                                                          case 1:
                                                        Readout.labelswithtime({fromDate: $scope.fromDate, toDate: $scope.toDate, sensor: $scope.s}, function(result){
                                                         $scope.labels = result;
                                                        });
                                                                            break;
                                                                          case 2:
                                                        Readout.labelswithtime({fromDate: $scope.fromDate2, toDate: $scope.toDate2, sensor: $scope.sensor2}, function(result){
                                                         $scope.labels2 = result;
                                                        });
                                                                              break;
                                                                          case 3:
                                                        Readout.labelswithtime({fromDate: $scope.fromDate3, toDate: $scope.toDate3, sensor: $scope.sensor3}, function(result){
                                                        $scope.labels3 = result;
                                                        });
                                                                              break;
                                                                          case 4:
                                                        Readout.labelswithtime({fromDate: $scope.fromDate4, toDate: $scope.toDate4, sensor: $scope.sensor4}, function(result){
                                                         $scope.labels4 = result;
                                                        });
                                                                              break;
                                                      }
         };

          $scope.loadAgents();
    });
