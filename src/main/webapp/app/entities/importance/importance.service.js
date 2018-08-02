(function() {
    'use strict';
    angular
        .module('civilDefanceApp')
        .factory('Importance', Importance);

    Importance.$inject = ['$resource'];

    function Importance ($resource) {
        var resourceUrl =  'api/importances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
