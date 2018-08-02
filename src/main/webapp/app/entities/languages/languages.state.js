(function() {
    'use strict';

    angular
        .module('civilDefanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('languages', {
            parent: 'entity',
            url: '/languages',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.languages.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/languages/languages.html',
                    controller: 'LanguagesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('languages');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('languages-detail', {
            parent: 'languages',
            url: '/languages/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'civilDefanceApp.languages.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/languages/languages-detail.html',
                    controller: 'LanguagesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('languages');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Languages', function($stateParams, Languages) {
                    return Languages.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'languages',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('languages-detail.edit', {
            parent: 'languages-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/languages/languages-dialog.html',
                    controller: 'LanguagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Languages', function(Languages) {
                            return Languages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('languages.new', {
            parent: 'languages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/languages/languages-dialog.html',
                    controller: 'LanguagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                languageDesc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('languages');
                });
            }]
        })
        .state('languages.edit', {
            parent: 'languages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/languages/languages-dialog.html',
                    controller: 'LanguagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Languages', function(Languages) {
                            return Languages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('languages.delete', {
            parent: 'languages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/languages/languages-delete-dialog.html',
                    controller: 'LanguagesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Languages', function(Languages) {
                            return Languages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
