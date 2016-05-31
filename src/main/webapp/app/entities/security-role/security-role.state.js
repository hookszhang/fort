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
                authorities: ['ROLE_USER'],
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
                authorities: ['ROLE_USER'],
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
                authorities: ['ROLE_USER']
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
                authorities: ['ROLE_USER']
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
                authorities: ['ROLE_USER']
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
