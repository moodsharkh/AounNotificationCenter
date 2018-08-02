
(function() {
    'use strict';
    angular
      .module('civilDefanceApp')

      .controller('NewLeafletMapsController', NewLeafletMapsController)
       ;

    NewLeafletMapsController.$inject = ['$scope', 'leafletData','$state','Border' ,'Points','Notification','$rootScope','$location', '$anchorScroll','$window'];

    function NewLeafletMapsController($scope, leafletData,$state,Border ,Points,Notification, $location, $anchorScroll) {

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
        var tempcoordinates=[];
        var drawnraduis;

        //adding drawing layer
        leafletData.getMap().then(function(map) {
            var  drawControl = new L.Control.Draw({
                draw : {
                    position : 'topleft',
                    polygon : false,
                    polyline : false,
                    rectangle : false,
                    circle : true,
                    marker:false

                },
                edit : false
            });

            map.addControl(drawControl);
        });


        //when the drawing layer activated the map
        leafletData.getMap().then(function(map) {

            leafletData.getLayers().then(function(baselayers) {

                var drawnItems = baselayers.overlays.draw;
                map.on('draw:created', function (e) {
                    $scope.reg.id = true;
                    var layer = e.layer;
                    drawnItems.addLayer(layer);
               //     console.log(JSON.stringify(layer.toGeoJSON()));

                    drawedcoordinates = layer.toGeoJSON().geometry.coordinates;

                    try{drawnraduis = layer.getRadius().toString();}
                    catch (ex){}


                    window.scrollTo(0,0);


                });
            });
        });

        //getting search engine
        leafletData.getMap().then(function(map) {

            map.addControl(new L.Control.Search({
                url: 'http://nominatim.openstreetmap.org/search?format=json&q={s}',
                jsonpParam: 'json_callback',
                propertyName: 'display_name',
                propertyLoc: ['lat', 'lon'],
                circleLocation: false,
                markerLocation: false,
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

            var count=0;
            var tempid;
                Notification.query(function(result) {
                    vm.notification = result;
                    vm.searchQuery = null;
                });
            for (var i=0;i< vm.borders.length;i++) {
                var poligonstring = [];
                var prio = 0;
                count = 0;
                for (var k=0;k< vm.notification.length;k++) {
                    if (vm.notification[k].notimanytoone.id==vm.borders[i].id) {
                        console.log(vm.notification[k].imanytoone.id)
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
                            tempcircle.bindPopup(vm.borders[i].desc)
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

                }

            }
            });
            map.setView([21.41734,39.82901],10);
        });


       var obj;
       var JsonFormat;
       var insertedId;
       var flag=0;

        $scope.saveBorder =   function  () {
          //  console.log("I am Here");
           // console.log(drawedcoordinates);

     //     var  morning ="{\"id\":null,\"description\":\"MyThing\",\"lat\":\""+lona+"\",\"lon\":\""+lata+"\",\"raduis\":\""+raduis+"\"}";
            if (drawedcoordinates != null) {
                console.log("I am Here");
                JsonFormat = "{\"desc\":\""+vm.border.desc+"\",\"insertionDate\":\"2017-09-15T21:00:00.000Z\",\"insertedBy\":\"Ali\",\"id\":null}";
                obj = JSON.parse(JsonFormat);
                if (obj.id !== null) {
                    Border.update(obj, onBorderSaveSuccess, onBorderSaveError);
                } else {
                    Border.save(obj, onBorderSaveSuccess, onBorderSaveError);
                }
            }
        }
        function onBorderSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:borderUpdate', result);
            //console.log(result);
            insertedId = result.id;

            preparingsavingPoints();
        }
        function onBorderSaveError () {

        }
        function preparingsavingPoints(){
            if (drawedcoordinates.length == 2){
             //   console.log("My Lenth is two");
                try{
                    JsonFormat = "{\"id\":null,\"lon\":\""+drawedcoordinates[1]+"\",\"lat\":\""+drawedcoordinates[0]+"\",\"raduis\":\""+drawnraduis+"\",\"manytoone\":{\"desc\":\""+vm.border.desc+"\",\"insertionDate\":\"2017-09-15T21:00:00.000Z\",\"insertedBy\":\"Ali\",\"id\":\""+insertedId+"\"}}"
               savepoint();
                }
                catch(ex){
                    console.log(ex);
                }
            }
            else {
         //       console.log("My Lenth is not two");
                drawedcoordinates = drawedcoordinates[0];
                tempcoordinates = drawedcoordinates[0];
                for (var i = 0; i<tempcoordinates.length;i++) {
                    for (var m = 0; m < drawedcoordinates.length; m++) {
                        if (drawedcoordinates[m][1]== tempcoordinates[i][1])
                            drawedcoordinates
                    }
                }
                for (var i = 0; i<drawedcoordinates.length;i++){
                    JsonFormat = "{\"id\":null,\"lon\":\""+drawedcoordinates[i][1]+"\",\"lat\":\""+drawedcoordinates[i][0]+"\",\"raduis\":null,\"manytoone\":{\"desc\":\""+vm.border.desc+"\",\"insertionDate\":\"2017-09-15T21:00:00.000Z\",\"insertedBy\":\"Ali\",\"id\":\""+insertedId+"\"}}"
               savepoint();
                }
            }
        }
        function savepoint () {

        console.log(JsonFormat);
            obj = JSON.parse(JsonFormat);
            if (vm.points.id !== null) {
                Points.update(obj, onpointSaveSuccess, onpointSaveError);
            } else {
                Points.save(obj, onpointSaveSuccess, onpointSaveError);
            }
        }
        function onpointSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:pointsUpdate', result);
            // console.log("I Am IN HERE");
            $state.go('border', null, { reload: true });

        }
        function onpointSaveError () {

        }

    }
})();

