
(function() {
    'use strict';
    angular
      .module('civilDefanceApp')

      .controller('LeafletMapsdialogController', LeafletMapsdialogController)
       ;

    LeafletMapsdialogController.$inject = ['$scope', 'leafletData','$uibModalInstance','$stateParams','entity','Border' ,'Points','Notification','$rootScope','$location', '$anchorScroll','$window'];

    function LeafletMapsdialogController($scope, leafletData,$uibModalInstance,$stateParams,entity,Border ,Points,Notification, $location, $anchorScroll) {

        var vm = this;

        vm.border = entity;

        vm.clear = clear;

        vm.points = [];

        loadAllPoints();
        vm.notification=[];
        loadAllNotifications();
        function loadAllNotifications() {
            Notification.query(function(result) {
                vm.notification = result;
                vm.searchQuery = null;
            });
        }
        //loads all points into array
        function loadAllPoints() {
            Points.query(function(result) {
                vm.points = result;
                vm.searchQuery = null;
            });
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        //base formats
        angular.extend($scope, {
            center: {
                autoDiscover: true,
                zoom: 10

            },
            controls: {

            },      layers: {
                baselayers: {
                    xyz: {
                        name: 'OpenStreetMap (XYZ)',
                        type: 'xyz',
                        url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',

                        layerParams: {
                            showOnSelector: false
                        }
                        ,
                        Control:{
                            attribution:true
                        }
                    },
                    mapbox_light: {
                        name: 'Mapbox Light',
                        type: 'geoJSONShape',
                        user: 'elesdoar',
                        key: 'citojtj9e00022iqjmdzhrdwd',
                        apiKey: 'pk.eyJ1IjoiZWxlc2RvYXIiLCJhIjoiY2l0bmcwaDNpMDQzMTJvbDRpaTltN2dlbiJ9.KDnhRVh9St6vpQovMI7iLg',
                        layerParams: {
                            showOnSelector: false
                        }
                    }
                },
                overlays: {
                    search: {
                        name: 'search',
                        type: 'group',
                        visible: true,
                        layerParams: {
                            showOnSelector: false
                        }
                    },
                    draw: {
                        name: 'draw',
                        type: 'group',
                        visible: true,
                        layerParams: {
                            showOnSelector: false
                        },

                    }
                }
            }


        });
        var drawedcoordinates=[];
        var drawnraduis;

        //drawing shapes from Database
        leafletData.getMap().then(function (map) {
            var count=0;
            var tempid;
            Notification.query(function(result) {
                vm.notification = result;
                vm.searchQuery = null;
            });
            Points.query(function(result) {
                vm.points = result;
                vm.searchQuery = null;

                var poligonstring = [];
                count = 0;
                for (var m = 0; m < vm.points.length; m++) {
                    if (vm.border.id == vm.points[m].manytoone.id) {
                        count++;
                        tempid = vm.points[m].id;
                       // console.log(tempid);
                    }
                }
                var prio = 0;
                for (var k=0;k< vm.notification.length;k++) {
                    if (vm.notification[k].notimanytoone.id==vm.border.id) {
                        console.log(vm.notification[k].imanytoone.id)
                        prio = vm.notification[k].imanytoone.id;
                    }

                }
                var color = '';
                if (prio ==1)
                    color = '#ff0000';
                else if (prio ==2)
                    color = '#ffa700';
                else if (prio == 3)
                    color = '#cacaca';
                else
                    color = '#285e68';
                if (count ==1) {
                    for (var m = 0; m < vm.points.length; m++) {
                        if (tempid == vm.points[m].id) {
                            var tempcircle = L.circle([vm.points[m].lon, vm.points[m].lat], vm.points[m].raduis, {
                                color: color,
                                fillColor: color,
                                fillOpacity: 0.2,


                            }).addTo(map);
                            tempcircle.bindPopup(vm.border.desc)
                            map.setView([vm.points[m].lon,vm.points[m].lat],10);
                        }
                    }

                }
                else
                {
                    for (var m = 0; m < vm.points.length; m++) {
                        if ( vm.border.id == vm.points[m].manytoone.id) {

                            var p = new L.LatLng(vm.points[m].lon,vm.points[m].lat) ;
                            poligonstring.push(p)
                            // map.setView([vm.points[m].lon,vm.points[m].lat],10);
                        }

                        }
                    var bounds = new L.LatLngBounds(poligonstring);
                    map.fitBounds(bounds);
                    //console.log(poligonstring);

                    var polygon = L.polygon(poligonstring).addTo(map);
                    polygon.setStyle({fillColor: color, color: color});
                    polygon.bindPopup(vm.border.desc)
                    // map.setView([vm.points[m].lon,vm.points[m].lat],10);
                }


            });
        });

    }
})();

