(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('BorderDialogController', BorderDialogController);

    BorderDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'leafletData','$uibModalInstance', 'entity', 'Border', 'Points', 'Notification'];

    function BorderDialogController ($timeout, $scope, $stateParams, leafletData,$uibModalInstance, entity, Border, Points, Notification) {
        var vm = this;
        var pointId = 0;
        var borderId = 0;
        vm.border = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.points = Points.query();
        vm.notifications = Notification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            if (drawedcoordinates.length>0){
                for (var i = 0; i < vm.points.length; i++) {
                    if (vm.points[i].manytoone.id == borderId) {
                        //     console.log("deleting");
                        Points.delete({id: vm.points[i].id});
                    }
                }

            preparingsavingPoints()
            }
            vm.isSaving = true;
            if (vm.border.id !== null) {
                Border.update(vm.border, onSaveSuccess, onSaveError);

            } else {
                Border.save(vm.border, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:borderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;

        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        var vm = this;

        vm.border = entity;

        vm.clear = clear;

        vm.points = [];
        borderId = vm.border.id;
        loadAllPoints();

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
            var drawnItems = new L.FeatureGroup();
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
                        pointId = vm.points[m].id;
                       // console.log(pointId);
                        var tempcircle = L.circle([vm.points[m].lon, vm.points[m].lat], vm.points[m].raduis, {
                            color: color,
                            fillColor: color,
                            fillOpacity: 0.2,


                        }).addTo(map);
                        tempcircle.bindPopup(vm.border.desc);
                        map.setView([vm.points[m].lon,vm.points[m].lat],10);
                        drawnItems.addLayer(tempcircle);
                    }
                }

            }
            else
            {
                for (var m = 0; m < vm.points.length; m++) {
                    if ( vm.border.id == vm.points[m].manytoone.id) {
                        pointId = vm.points[m].id;
                        console.log(pointId);
                        var p = new L.LatLng(vm.points[m].lon,vm.points[m].lat) ;
                        poligonstring.push(p)

                    }

                }
                var bounds = new L.LatLngBounds(poligonstring);
                map.fitBounds(bounds);

                var polygon = L.polygon(poligonstring).addTo(map);
                polygon.setStyle({fillColor: color, color: color});
                polygon.bindPopup(vm.border.desc);
                drawnItems.addLayer(polygon);

            }



            map.addLayer(drawnItems);


            L.DrawToolbar.include({
                getModeHandlers : function(map) {
                    return [ {
                        enabled : false,
                        handler : new L.Draw.Polygon(map),
                        title : L.drawLocal.draw.toolbar.buttons.polygon
                    },{
                        enabled : this.options.circle,
                        handler : new L.Draw.Circle(map,
                            this.options.circle),
                        title : L.drawLocal.draw.toolbar.buttons.circle
                    } ];
                }
            });

            // Initialize the draw control and pass it the FeatureGroup of editable layers
            var drawControl = new L.Control.Draw({
                position : 'topleft',
                edit:{
                    featureGroup : drawnItems}
            });
            map.addControl(drawControl);

            map.on('draw:created', function(e) {

                var type = e.layerType, layer = e.layer;
                drawedcoordinates = layer.toGeoJSON().geometry.coordinates;
                try{drawnraduis = layer.getRadius().toString();}
                catch (ex){}
                drawnItems.addLayer(layer);
              //   console.log(JSON.stringify(layer.toGeoJSON()));
            });
            map.on('draw:edited', function(e) {
                var layers = e.layers;
                layers.eachLayer(function (layer) {
                    drawedcoordinates = layer.toGeoJSON().geometry.coordinates;
                    try{drawnraduis = layer.getRadius().toString();}
                    catch (ex){}
                    for (var i =0 ;i<vm.points.length;i++)
                    {
                        if (vm.points[i].manytoone.id == borderId) {
                       //     console.log("deleting");
                            Points.delete({id: vm.points[i].id});
                        }
                        map.setView([vm.points[m].lon,vm.points[m].lat],10);
                    }
                    preparingsavingPoints();
                  //  console.log("I am here "+JSON.stringify(layer.toGeoJSON()));
                });

            });

          //  map.setView([24.70973,46.68140],10);
            });
        });
        var JsonFormat;
        var obj;
        function preparingsavingPoints(){
            if (drawedcoordinates.length == 2){
                //   console.log("My Lenth is two");
                try{
                    JsonFormat = "{\"id\":null,\"lon\":\""+drawedcoordinates[1]+"\",\"lat\":\""+drawedcoordinates[0]+"\",\"raduis\":\""+drawnraduis+"\",\"manytoone\":{\"desc\":\""+vm.border.desc+"\",\"insertionDate\":\"2017-09-15T21:00:00.000Z\",\"insertedBy\":\"Ali\",\"id\":\""+vm.border.id+"\"}}"
                    savepoint();
                }
                catch(ex){
                    console.log(ex);
                }
            }
            else {
                //       console.log("My Lenth is not two");
                drawedcoordinates = drawedcoordinates[0];
                for (var i = 0; i<drawedcoordinates.length;i++){
                    JsonFormat = "{\"id\":null,\"lon\":\""+drawedcoordinates[i][1]+"\",\"lat\":\""+drawedcoordinates[i][0]+"\",\"raduis\":null,\"manytoone\":{\"desc\":\""+vm.border.desc+"\",\"insertionDate\":\"2017-09-15T21:00:00.000Z\",\"insertedBy\":\"Ali\",\"id\":\""+vm.border.id+"\"}}"
                    savepoint();
                }
            }
        }
        function savepoint () {

          //  console.log(JsonFormat);
            obj = JSON.parse(JsonFormat);
            if (vm.points.id !== null) {
             //   console.log("I am saving : "+ vm.border.id);
                Points.update(obj, onpointSaveSuccess, onpointSaveError);
            } else {
                console.log("I am saving : "+ vm.border.id);
                Points.save(obj, onpointSaveSuccess, onpointSaveError);
            }
        }
        function onpointSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:pointsUpdate', result);
            // console.log("I Am IN HERE");
        //    $state.go('border', null, { reload: true });

        }
        function onpointSaveError () {

        }
    }
})();
