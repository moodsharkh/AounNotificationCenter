(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification', {
            parent: 'entity',
            url: '/notification',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notifications.html',
                    controller: 'NotificationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-detail', {
            parent: 'notification',
            url: '/notification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notification-detail.html',
                    controller: 'NotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Notification', function($stateParams, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notification-detail.edit', {
            parent: 'notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('notification.new', {
                parent: 'notification',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'civilDefanceApp.notification.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notification/notification-dialog.html',
                        controller: 'NotificationDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notification');
                        return $translate.refresh();
                    }],
                    entity: function () {
            return {
                title: null,
                message: null,
                receivedBy: null,
                sendingDate: null,
                expiryDate: null,
                id: null
            };

        },
                    entity2: function () {
                        return {
                            title: null,
                            message: null,
                            receivedBy: null,
                            sendingDate: null,
                            expiryDate: null,
                            id: null
                        };

                    },
                    entity3: function () {
                        return {
                            title: null,
                            message: null,
                            receivedBy: null,
                            sendingDate: null,
                            expiryDate: null,
                            id: null
                        };

                    }

    },
        previousState: ["$state", function ($state) {
            var currentStateData = {
                name: $state.current.name || 'notification',
                params: $state.params,
                url: $state.href($state.current.name, $state.params)
            };
            return currentStateData;
        }]
    })
        .state('notification.edit', {
            parent: 'notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification.delete', {
            parent: 'notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-delete-dialog.html',
                    controller: 'NotificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
