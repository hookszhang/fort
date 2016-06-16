(function() {
    'use strict';

    angular
        .module('fortApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('onekey-login', {
            parent: 'app',
            url: '/onekey-login?u&p',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/onekey-login/onekey-login.html',
                    controller: 'OneKeyLoginController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
