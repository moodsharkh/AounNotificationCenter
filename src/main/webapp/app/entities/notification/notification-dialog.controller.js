(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .controller('NotificationDialogController', NotificationDialogController);

    NotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'entity2','entity3','Notification', 'Border', 'Importance','Messages','Languages','DateUtils','$state','$http',];

    function NotificationDialogController ($timeout, $scope, $stateParams, entity,entity2,entity3, Notification, Border, Importance,Messages, Languages,DateUtils,$state,$http) {
        var vm = this;
        vm.languages = Languages.query();
        vm.languages2 = Languages.query();
        vm.updatedNotification = entity;
        vm.newmessage1 ;
        $scope.dateOptions = {
            minDate: new Date()
        };
        var border2list = [];
        $scope.reg={first:false};
        $scope.reg={second:false};
        $scope.reg={priority: false};
        $scope.reg={region: false};
        $scope.lan={message_En: false};
        $scope.lan={message_Ur: false};
        $scope.lan={Message_First: false};
        $scope.lan={Message_Second: false};
        $scope.reg={more: true};

       $scope.lan={MoreLanguage: true};

        //More Area Function
        $scope.regCheck = function(){
            // console.log("I am here");
            $scope.reg.more = true;
            if ($scope.reg.first==true)
                $scope.reg.second = true;
            else
                $scope.reg.first = true;
            if ($scope.reg.second==true && $scope.reg.first == true)
                $scope.reg.more = false;
            for ( var i = 0; i < vm.borders2.length; i++) {
                if (vm.borders2[i].id == vm.notification.notimanytoone.id) {
                    vm.borders2.splice(i, 1);

                }
            }
        if (vm.notification2.notimanytoone !== undefined) {
            // vm.borders3 = vm.borders2;
             for (var i = 0; i < vm.borders3.length; i++) {

                     if (vm.borders3[i].id == vm.notification2.notimanytoone.id ) {
                         vm.borders3.splice(i, 1);
                     }
                 }
            for (var i = 0; i < vm.borders3.length; i++) {

                if (vm.borders3[i].id == vm.notification.notimanytoone.id ) {
                    vm.borders3.splice(i, 1);
                }
            }
            }
        };
        $scope.regclear2= function () {
            $scope.reg.first=false;
            vm.notification2.notimanytoone =undefined;
            if ($scope.reg.more == false)
                $scope.reg.more = true;
        };
        $scope.regclear3= function () {
            $scope.reg.second = false;
            vm.notification3.notimanytoone= undefined;
            if ($scope.reg.more == false)
                $scope.reg.more = true;
        };



        //Language Check Area
        $scope.LanguaguesCheck= function () {
            if ($scope.lan.Message_First==true)
                $scope.lan.Message_Second = true;
            else
                $scope.lan.Message_First = true;
            if ( $scope.lan.message_En==true && $scope.lan.message_Ur == true)
                $scope.lan.MoreLanguage = false;
        };
        $scope.FirstLan = function () {
            if (vm.newmessage1.notificationToLanguages!= undefined) {
                if (vm.newmessage1.notificationToLanguages.id == 2) {
                    $scope.lan.message_En = true;
                    $scope.lan.Message_First = false;
                    $scope.lan.Message_Second = false;
                    for (var i = 0; i < vm.languages.length; i++) {

                        if (vm.languages[i].id == vm.newmessage1.notificationToLanguages.id) {
                            vm.languages.splice(i, 1);
                        }
                    }
                }
                else if (vm.newmessage1.notificationToLanguages.id == 3) {
                    $scope.lan.message_Ur = true;
                    $scope.lan.Message_First = false;
                    $scope.lan.Message_Second = false;
                    for (var i = 0; i < vm.languages.length; i++) {

                        if (vm.languages[i].id == vm.newmessage1.notificationToLanguages.id) {
                            vm.languages.splice(i, 1);
                        }
                    }
                }
                if ($scope.lan.message_En == true && $scope.lan.message_Ur == true)
                    $scope.lan.MoreLanguage = false;
            }
        };
        vm.notification = entity;
        vm.notification2=entity2;
        vm.notification3 = entity3;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.borders = Border.query();
        vm.borders2 = Border.query();
        vm.borders3 = Border.query();
        vm.importances = Importance.query();
        // orgnize();

        loadAll();
    vm.borderso=[];
        vm.borderi;
        function loadAll() {

        }
function onSS(result) {
    $scope.$emit('civilDefanceApp:borderUpdate', result);
}
function onSE() {

}
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('notification', null, { reload: true });
        }

        function save () {
            // Fields Validation
            if (vm.notification.notimanytoone == undefined  ){
                $scope.reg.region = true;
            }
            else if(vm.notification.imanytoone == undefined){$scope.reg.priority = true;
                $scope.reg.region = false; }
            else {
            // vm.isSaving = true;
            // console.log(vm.notification);



                if (vm.notification2 !== null) {
                    // console.log(vm.notification.imanytoone);
                    vm.notification2.title = vm.notification.title;
                    vm.notification2.message = vm.notification.message;
                    vm.notification2.sendingDate = vm.notification.sendingDate;
                    vm.notification2.receivedBy = vm.notification.receivedBy;
                    vm.notification2.expiryDate = vm.notification.expiryDate;
                    vm.notification2.imanytoone = vm.notification.imanytoone;
                }
                if (vm.notification3 !== null) {
                    vm.notification3.title = vm.notification.title;
                    vm.notification3.message = vm.notification.message;
                    vm.notification3.sendingDate = vm.notification.sendingDate;
                    vm.notification3.receivedBy = vm.notification.receivedBy;
                    vm.notification3.expiryDate = vm.notification.expiryDate;
                    vm.notification3.imanytoone = vm.notification.imanytoone;
                }

                if (vm.notification.id !== null) {
                    Notification.save(vm.notification, onSaveSuccess, onSaveError);
                } else {
                    Notification.save(vm.notification, onSaveSuccess, onSaveError);
                    if (vm.notification2.notimanytoone !== undefined)
                        Notification.save(vm.notification2, onSaveSuccess, onSaveError);
                    if (vm.notification3.notimanytoone !== undefined)
                        Notification.save(vm.notification3, onSaveSuccess, onSaveError);

                }

            }
        }

        var insertedId;
         vm.obj;
        vm.JsonFormat;
        function onSaveSuccess (result) {
            // $scope.$emit('civilDefanceApp:notificationUpdate', result);
            vm.updatedNotification.id = result.id;
            vm.updatedNotification.title = result.title;
            vm.updatedNotification.message = result.message;
            vm.updatedNotification.sendingDate = result.sendingDate;
            vm.updatedNotification.receivedBy = result.receivedBy;
            vm.updatedNotification.expiryDate = result.expiryDate;
            vm.updatedNotification.imanytoone = result.imanytoone;
            vm.updatedNotification.notimanytoone = result.notimanytoone;
            insertedId = result.id;
            preparesaveMessage();
            console.log(vm.updatedNotification);

            Notification.update(vm.updatedNotification, afterSaveSuccess, onSaveError);
            // vm.isSaving = false;
            $state.go('notification', null, { reload: 'notification' });
        }
function  preparesaveMessage() {
    // vm.notification.expiryDate = DateUtils.convertLocalDateToServer(vm.notification.expiryDate);
    // vm.notification.expiryDate = vm.notification.expiryDate+"T21:00:00.000Z";
    if (vm.newmessage1 != undefined || vm.newmessage1 != undefined){
        vm.JsonFormat = "{\"title\":\""+vm.newmessage1.title+"\",\"messageContent\":\""+vm.newmessage1.message+"\",\"id\":null,\"messageToLanguages\":{\"id\":2,\"languageDesc\":\"English\"},\"messageToNotification\":{\"id\":"+insertedId+",\"title\":\""+vm.notification.title+"\",\"message\":\""+vm.notification.message+"\",\"receivedBy\":null,\"sendingDate\":null,\"expiryDate\":\""+""+"\",\"notimanytoone\":{\"id\":"+vm.notification.notimanytoone.id+",\"desc\":\""+vm.notification.notimanytoone.desc+"\",\"insertionDate\":\""+vm.notification.notimanytoone.insertionDate+"\",\"insertedBy\":\""+vm.notification.notimanytoone.insertedBy+"\"},\"imanytoone\":{\"id\":"+vm.notification.imanytoone.id+"\,\"desc\":\""+vm.notification.imanytoone.desc+"\"}}}";

    saveMessage();
    }
        if (vm.newmessage2 != undefined || vm.newmessage2 != undefined) {
            vm.JsonFormat = "{\"title\":\"" + vm.newmessage2.title + "\",\"messageContent\":\"" + vm.newmessage2.message + "\",\"id\":null,\"messageToLanguages\":{\"id\":3,\"languageDesc\":\"Urdu\"},\"messageToNotification\":{\"id\":\"" + insertedId + "\",\"title\":\"" + vm.notification.title + "\",\"message\":\"" + vm.notification.message + "\",\"receivedBy\":null,\"sendingDate\":null,\"expiryDate\":\"" + "" + "\",\"notimanytoone\":{\"id\":" + vm.notification.notimanytoone.id + ",\"desc\":\"" + vm.notification.notimanytoone.desc + "\",\"insertionDate\":\"" + vm.notification.notimanytoone.insertionDate + "\",\"insertedBy\":\"" + vm.notification.notimanytoone.insertedBy + "\"},\"imanytoone\":{\"id\":" + vm.notification.imanytoone.id + ",\"desc\":\"" + vm.notification.imanytoone.desc + "\"}}}";
    saveMessage();
    }
    Border.query(function(result) {
        vm.borderso = result;
        vm.searchQuery = null;
        for (var i=0; i<vm.borderso.length;i++){
            if (vm.borderso[i].id == vm.notification.notimanytoone.id)
                vm.borderi = vm.borderso[i];

        }
        vm.borderi.colorReset = 0;
        Border.update(vm.borderi, onSS, onSE);
    });


}
        function afterSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:notificationUpdate', result);
        }
        function onSaveError () {
            // vm.isSaving = false;
        }
        function saveMessage () {
            vm.obj = JSON.parse(vm.JsonFormat);

                Messages.save(vm.obj, onMessageSaveSuccess, onMessageSaveError);

        }

        function onMessageSaveSuccess (result) {
            $scope.$emit('civilDefanceApp:messagesUpdate', result);
            // vm.isSaving = false;
        }
        function onMessageSaveError () {
            // vm.isSaving = false;
        }
        vm.datePickerOpenStatus.sendingDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }





    }
})();
