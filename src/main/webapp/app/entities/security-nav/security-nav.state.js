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
        .state('security-nav', {
            parent: 'entity',
            url: '/security-nav?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityNav.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-nav/security-navs.html',
                    controller: 'SecurityNavController',
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
                    $translatePartialLoader.addPart('securityNav');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-nav-detail', {
            parent: 'entity',
            url: '/security-nav/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityNav.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-nav/security-nav-detail.html',
                    controller: 'SecurityNavDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityNav');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityNav', function($stateParams, SecurityNav) {
                    return SecurityNav.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-nav.new', {
            parent: 'security-nav',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-nav/security-nav-dialog.html',
                    controller: 'SecurityNavDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-nav.edit', {
            parent: 'security-nav',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-nav/security-nav-dialog.html',
                    controller: 'SecurityNavDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-nav.delete', {
            parent: 'security-nav',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-nav/security-nav-delete-dialog.html',
                    controller: 'SecurityNavDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityNav', function(SecurityNav) {
                            return SecurityNav.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-nav', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
