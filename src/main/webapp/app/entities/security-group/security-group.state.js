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
                authorities: ['ROLE_USER'],
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
                authorities: ['ROLE_USER'],
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
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-group/security-group-dialog.html',
                    controller: 'SecurityGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                st: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('security-group', null, { reload: true });
                }, function() {
                    $state.go('security-group');
                });
            }]
        })
        .state('security-group.edit', {
            parent: 'security-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/security-group/security-group-dialog.html',
                    controller: 'SecurityGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
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
        })
        .state('security-group.delete', {
            parent: 'security-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
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
