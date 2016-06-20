(function() {
    'use strict';

    angular
        .module('fortApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('security-resource-entity', {
            parent: 'entity',
            url: '/security-resource-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityResourceEntity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-resource-entity/security-resource-entities.html',
                    controller: 'SecurityResourceEntityController',
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
                    $translatePartialLoader.addPart('securityResourceEntity');
                    $translatePartialLoader.addPart('resourceEntityType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('security-resource-entity-detail', {
            parent: 'entity',
            url: '/security-resource-entity/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP'],
                pageTitle: 'fortApp.securityResourceEntity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-resource-entity/security-resource-entity-detail.html',
                    controller: 'SecurityResourceEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('securityResourceEntity');
                    $translatePartialLoader.addPart('resourceEntityType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SecurityResourceEntity', function($stateParams, SecurityResourceEntity) {
                    return SecurityResourceEntity.get({id : $stateParams.id});
                }]
            }
        })
        .state('security-resource-entity.new', {
            parent: 'security-resource-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-resource-entity/security-resource-entity-dialog.html',
                    controller: 'SecurityResourceEntityDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-resource-entity.edit', {
            parent: 'security-resource-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/security-resource-entity/security-resource-entity-dialog.html',
                    controller: 'SecurityResourceEntityDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('security-resource-entity.delete', {
            parent: 'security-resource-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_SECURITY_APP']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-resource-entity/security-resource-entity-delete-dialog.html',
                    controller: 'SecurityResourceEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SecurityResourceEntity', function(SecurityResourceEntity) {
                            return SecurityResourceEntity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('security-resource-entity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
