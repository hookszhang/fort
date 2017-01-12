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
        .state('security-authority', {
            parent: 'entity',
            url: '/security-authority?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityAuthority.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-authority/security-authorities.html',
                    controller: 'SecurityAuthorityController',
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
                    $translatePartialLoader.addPart('securityAuthority');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-authority-detail', {
            parent: 'entity',
            url: '/security-authority/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityAuthority.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-authority/security-authority-detail.html',
                    controller: 'SecurityAuthorityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityAuthority');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityAuthority', function($stateParams, SecurityAuthority) {
                    return SecurityAuthority.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-authority-new', {
            parent: 'security-authority',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityAuthority.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-authority/security-authority-dialog.html',
                    controller: 'SecurityAuthorityDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-authority.edit', {
            parent: 'security-authority',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-authority/security-authority-dialog.html',
                    controller: 'SecurityAuthorityDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-authority.delete', {
            parent: 'security-authority',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-authority/security-authority-delete-dialog.html',
                    controller: 'SecurityAuthorityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityAuthority', function(SecurityAuthority) {
                            return SecurityAuthority.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-authority', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
