
(function() {
    'use strict';
    angular
      .module('civilDefanceApp')

      .controller('LeafletMapsController', LeafletMapsController)
       ;

    LeafletMapsController.$inject = ['$scope', 'Border','leafletData' ,'Points','Notification','$rootScope','$location', '$anchorScroll','$window'];

    function LeafletMapsController($scope,Border, leafletData ,Points,Notification, $location, $anchorScroll) {

        var vm = this;

        vm.borders = [];
        $scope.reg = {id: false};
        // loadAll();
        //loading all borders into array
        function loadAll() {
            Border.query(function(result) {
                vm.borders = result;
                vm.searchQuery = null;
            });
        }

        vm.points = [];

        loadAllPoints();

        //loads all points into array
        function loadAllPoints() {
            Points.query(function(result) {
                vm.points = result;
                vm.searchQuery = null;
            });
        }
        vm.notification=[];
        loadAllNotifications();
        function loadAllNotifications() {
            Notification.query(function(result) {
                vm.notification = result;
                vm.searchQuery = null;
            });
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

        //adding drawing layer





        //getting search engine
        leafletData.getMap().then(function(map) {

            map.addControl(new L.Control.Search({
                url: 'http://nominatim.openstreetmap.org/search?format=json&q={s}',
                jsonpParam: 'json_callback',
                propertyName: 'display_name',
                propertyLoc: ['lat', 'lon'],
                circleLocation: true,
                markerLocation: true,
                autoType: false,
                autoCollapse: true,
                minLength: 2,
                zoom: 10
            }));
        });


        //drawing shapes from Database
        leafletData.getMap().then(function (map) {
            Border.query(function(result) {
                vm.borders = result;
                vm.searchQuery = null;

                Points.query(function(result) {
                    vm.points = result;
                    vm.searchQuery = null;

                    Notification.query(function(result) {
                        vm.notification = result;
                        vm.searchQuery = null;
                    });
                var count=0;
                var tempid;

                for (var i=0;i< vm.borders.length;i++) {

                    var poligonstring = [];
                    count = 0;
                    var prio = 0;
                    for (var k=0;k< vm.notification.length;k++) {
                        if (vm.notification[k].notimanytoone.id==vm.borders[i].id) {
                            // console.log(vm.notification[k].imanytoone.id)
                            prio = vm.notification[k].imanytoone.id;
                        }

                    }
                    for (var m = 0; m < vm.points.length; m++) {
                        if (vm.borders[i].id == vm.points[m].manytoone.id) {
                            count++;
                            tempid = vm.points[m].id;
                            // console.log(tempid);
                        }
                    }
                    //  console.log("count =  "+count);
                    var color = '';
                    if (prio ==1)
                        color = '#ff0000';
                    else if (prio ==2)
                        color = '#ffa700';
                    else if (prio == 3)
                        color = '#cacaca';
                    else
                        color = '#285e68';
                    if(vm.borders[i].colorReset==true)
                        color = '#285e68';
                    if (count ==1) {
                        for (var m = 0; m < vm.points.length; m++) {
                            if (tempid == vm.points[m].id) {
                                var tempcircle = L.circle([vm.points[m].lon, vm.points[m].lat], vm.points[m].raduis, {
                                    color: color,
                                    fillColor: color,
                                    fillOpacity: 0.2,


                                }).addTo(map);
                                tempcircle.bindPopup(vm.borders[i].desc)
                                tempcircle.on('mouseover',function (e) {
                                    this.openPopup();
                                });
                                tempcircle.on('mouseout',function (e) {
                                    this.openPopup();
                                });
                            }
                        }

                    }
                    else
                    {
                        for (var m = 0; m < vm.points.length; m++) {
                            if ( vm.borders[i].id == vm.points[m].manytoone.id) {

                                var p = new L.LatLng(vm.points[m].lon,vm.points[m].lat) ;
                                poligonstring.push(p)

                            }

                        }

                        // console.log(poligonstring);
                        var polygon = L.polygon(poligonstring).addTo(map);
                        polygon.setStyle({fillColor: color, color: color});
                        polygon.bindPopup(vm.borders[i].desc)
                        polygon.on('mouseover',function (e) {
                            this.openPopup();
                        });
                        polygon.on('mouseout',function (e) {
                            this.openPopup();
                        });

                    }

                }

            });
            });

            map.setView([21.41734,39.82901],11);
            // map.setView([24.70973,46.68140],15);
        });




    }
})();

