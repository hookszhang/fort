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
        .state('security-app', {
            parent: 'entity',
            url: '/security-app?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityApp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-app/security-apps.html',
                    controller: 'SecurityAppController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityApp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-app-detail', {
            parent: 'entity',
            url: '/security-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityApp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-app/security-app-detail.html',
                    controller: 'SecurityAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityApp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityApp', function($stateParams, SecurityApp) {
                    return SecurityApp.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-app.new', {
            parent: 'security-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-app/security-app-dialog.html',
                    controller: 'SecurityAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                appName: null,
                                appKey: null,
                                appSecret: null,
                                st: null,
                                id: null,
                                maxSessions: 2,
                                sessionMaxAge: 7
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('security-app', null, { reload: true });
                }, function() {
                    $state.go('security-app');
                });
            }]
        })
        .state('security-app.edit', {
            parent: 'security-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-app/security-app-dialog.html',
                    controller: 'SecurityAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SecurityApp', function(SecurityApp) {
                            return SecurityApp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-app', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('security-app.delete', {
            parent: 'security-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-app/security-app-delete-dialog.html',
                    controller: 'SecurityAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityApp', function(SecurityApp) {
                            return SecurityApp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-app', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('security-app.reset', {
            parent: 'security-app',
            url: '/{id}/rest',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-app/security-app-reset-dialog.html',
                    controller: 'SecurityAppResetController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityApp', function(SecurityApp) {
                            return SecurityApp.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-app', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
