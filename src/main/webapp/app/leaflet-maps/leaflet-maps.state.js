(function() {
  'use strict';

  angular
    .module('civilDefanceApp')
    .config(stateConfig);

  stateConfig.$inject = ['$stateProvider'];

  function stateConfig($stateProvider) {
    $stateProvider.state('leaflet-maps', {
      parent: 'app',
      url: '/',
      data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'civilDefanceApp.border.home.leaflet'
      },
      views: {
        'content@': {
          templateUrl: 'app/leaflet-maps/leaflet-maps.html',
          controller: 'LeafletMapsController',
          controllerAs: 'vm'
        }
      },
      resolve: {}
    }).state('newleaflet-maps', {
        parent: 'border',
        url: '/New',
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'civilDefanceApp.border.home.newleaflet'
        },
        views: {
            'content@': {
                templateUrl: 'app/leaflet-maps/Newleaflet-maps.html',
                controller: 'NewLeafletMapsController',
                controllerAs: 'vm'
            }
        },
        resolve: {}
    })
     .state('leaflet-maps-dialog', {
        parent: 'border',
        url: '/{id}/view',
        data: {
            authorities: ['ROLE_USER']
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
            $uibModal.open({
                templateUrl: 'app/leaflet-maps/leaflet-maps-dialog.html',
                controller: 'LeafletMapsdialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: ['Border', function(Border) {

                        return Border.get({id : $stateParams.id}).$promise;
                    }]
                }
            }).result.then(function() {
                // $state.go('^', {}, { reload: false });
            }, function() {
                // $state.go('^');
            });
        }]
    })
        .state('leaflet-maps-dialogn', {
            parent: 'notification',
            url: '/{id}/view',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/leaflet-maps/leaflet-maps-dialog.html',
                    controller: 'LeafletMapsdialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Border', function(Border) {

                            return Border.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    // $state.go('^', {}, { reload: false });
                }, function() {
                    // $state.go('^');
                });
            }]
        })
    ;
  }
})();
