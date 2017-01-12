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
        .state('security-role', {
            parent: 'entity',
            url: '/security-role?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityRole.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-role/security-roles.html',
                    controller: 'SecurityRoleController',
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
                    $translatePartialLoader.addPart('securityRole');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-role-detail', {
            parent: 'entity',
            url: '/security-role/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityRole.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-role/security-role-detail.html',
                    controller: 'SecurityRoleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityRole');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityRole', function($stateParams, SecurityRole) {
                    return SecurityRole.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-role.new', {
            parent: 'security-role',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-role/security-role-dialog.html',
                    controller: 'SecurityRoleDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-role.edit', {
            parent: 'security-role',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-role/security-role-dialog.html',
                    controller: 'SecurityRoleDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-role.delete', {
            parent: 'security-role',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-role/security-role-delete-dialog.html',
                    controller: 'SecurityRoleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityRole', function(SecurityRole) {
                            return SecurityRole.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-role', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
