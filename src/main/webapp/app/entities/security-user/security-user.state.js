(function() {
    'use strict';

    angular
        .module('fortApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('security-user', {
            parent: 'entity',
            url: '/security-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-user/security-users.html',
                    controller: 'SecurityUserController',
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
                    $translatePartialLoader.addPart('securityUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-user-detail', {
            parent: 'entity',
            url: '/security-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-user/security-user-detail.html',
                    controller: 'SecurityUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityUser', function($stateParams, SecurityUser) {
                    return SecurityUser.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-user.new', {
            parent: 'security-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-user/security-user-dialog.html',
                    controller: 'SecurityUserDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-user.edit', {
            parent: 'security-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-user/security-user-dialog.html',
                    controller: 'SecurityUserDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-user.delete', {
            parent: 'security-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-user/security-user-delete-dialog.html',
                    controller: 'SecurityUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityUser', function(SecurityUser) {
                            return SecurityUser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
