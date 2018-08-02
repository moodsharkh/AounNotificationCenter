(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('border', {
            parent: 'entity',
            url: '/border',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.border.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/border/borders.html',
                    controller: 'BorderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('border');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('border-detail', {
            parent: 'border',
            url: '/border/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.border.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/border/border-detail.html',
                    controller: 'BorderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('border');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Border', function($stateParams, Border) {
                    return Border.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'border',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('border-detail.edit', {
            parent: 'border-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/border/border-dialog.html',
                    controller: 'BorderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Border', function(Border) {
                            return Border.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('border.new', {
            parent: 'border',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/border/border-dialog.html',
                    controller: 'BorderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                desc: null,
                                insertionDate: null,
                                insertedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('border', null, { reload: 'border' });
                }, function() {
                    $state.go('border');
                });
            }]
        })
        .state('border.edit', {
            parent: 'border',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/border/border-dialog.html',
                    controller: 'BorderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Border', function(Border) {
                            return Border.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('border', null, { reload: 'border' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('border.delete', {
            parent: 'border',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/border/border-delete-dialog.html',
                    controller: 'BorderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Border', function(Border) {
                            return Border.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('border', null, { reload: 'border' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
