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
        .state('security-group', {
            parent: 'entity',
            url: '/security-group?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-group/security-groups.html',
                    controller: 'SecurityGroupController',
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
                    $translatePartialLoader.addPart('securityGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-group-detail', {
            parent: 'entity',
            url: '/security-group/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-group/security-group-detail.html',
                    controller: 'SecurityGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityGroup', function($stateParams, SecurityGroup) {
                    return SecurityGroup.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-group.new', {
            parent: 'security-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-group/security-group-dialog.html',
                    controller: 'SecurityGroupDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-group.edit', {
            parent: 'security-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-group/security-group-dialog.html',
                    controller: 'SecurityGroupDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-group.delete', {
            parent: 'security-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-group/security-group-delete-dialog.html',
                    controller: 'SecurityGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityGroup', function(SecurityGroup) {
                            return SecurityGroup.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-group', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
