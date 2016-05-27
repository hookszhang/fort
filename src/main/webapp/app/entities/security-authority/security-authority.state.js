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
                authorities: ['ROLE_USER'],
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
                authorities: ['ROLE_USER'],
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
            parent: 'entity',
            url: '/security-authority-new',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fortApp.securityAuthority.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-authority/security-authority-dialog.html',
                    controller: 'SecurityAuthorityDialogController',
                    controllerAs: 'vm'
                }
            }
            // onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
            //     $uibModal.open({
            //         templateUrl: 'app/entities/security-authority/security-authority-dialog.html',
            //         controller: 'SecurityAuthorityDialogController',
            //         controllerAs: 'vm',
            //         backdrop: 'static',
            //         size: 'lg',
            //         resolve: {
            //             entity: function () {
            //                 return {
            //                     name: null,
            //                     description: null,
            //                     st: null,
            //                     id: null
            //                 };
            //             }
            //         }
            //     }).result.then(function() {
            //         $state.go('security-authority', null, { reload: true });
            //     }, function() {
            //         $state.go('security-authority');
            //     });
            // }
            //]
        })
        .state('security-authority.edit', {
            parent: 'security-authority',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-authority/security-authority-dialog.html',
                    controller: 'SecurityAuthorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
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
        })
        .state('security-authority.delete', {
            parent: 'security-authority',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
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
