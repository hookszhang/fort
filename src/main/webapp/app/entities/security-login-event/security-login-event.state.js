/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function() {
    'use strict';

    angular
        .module('fortApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('security-login-event', {
            parent: 'entity',
            url: '/security-login-event',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityLoginEvent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-login-event/security-login-events.html',
                    controller: 'SecurityLoginEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityLoginEvent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-login-event-detail', {
            parent: 'entity',
            url: '/security-login-event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityLoginEvent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-login-event/security-login-event-detail.html',
                    controller: 'SecurityLoginEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityLoginEvent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityLoginEvent', function($stateParams, SecurityLoginEvent) {
                    return SecurityLoginEvent.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-login-event.new', {
            parent: 'security-login-event',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-login-event/security-login-event-dialog.html',
                    controller: 'SecurityLoginEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tokenValue: null,
                                tokenOverdueTime: null,
                                ipAddress: null,
                                userAgent: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('security-login-event', null, { reload: true });
                }, function() {
                    $state.go('security-login-event');
                });
            }]
        })
        .state('security-login-event.edit', {
            parent: 'security-login-event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-login-event/security-login-event-dialog.html',
                    controller: 'SecurityLoginEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SecurityLoginEvent', function(SecurityLoginEvent) {
                            return SecurityLoginEvent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-login-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('security-login-event.delete', {
            parent: 'security-login-event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-login-event/security-login-event-delete-dialog.html',
                    controller: 'SecurityLoginEventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityLoginEvent', function(SecurityLoginEvent) {
                            return SecurityLoginEvent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-login-event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
