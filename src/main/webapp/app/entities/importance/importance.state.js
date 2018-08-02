(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('importance', {
            parent: 'entity',
            url: '/importance',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.importance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/importance/importances.html',
                    controller: 'ImportanceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('importance');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('importance-detail', {
            parent: 'importance',
            url: '/importance/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.importance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/importance/importance-detail.html',
                    controller: 'ImportanceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('importance');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Importance', function($stateParams, Importance) {
                    return Importance.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'importance',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('importance-detail.edit', {
            parent: 'importance-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/importance/importance-dialog.html',
                    controller: 'ImportanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Importance', function(Importance) {
                            return Importance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('importance.new', {
            parent: 'importance',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/importance/importance-dialog.html',
                    controller: 'ImportanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                desc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('importance', null, { reload: 'importance' });
                }, function() {
                    $state.go('importance');
                });
            }]
        })
        .state('importance.edit', {
            parent: 'importance',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/importance/importance-dialog.html',
                    controller: 'ImportanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Importance', function(Importance) {
                            return Importance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('importance', null, { reload: 'importance' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('importance.delete', {
            parent: 'importance',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/importance/importance-delete-dialog.html',
                    controller: 'ImportanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Importance', function(Importance) {
                            return Importance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('importance', null, { reload: 'importance' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
