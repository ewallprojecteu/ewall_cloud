'use strict';

var twitterHome = angular.module('twitterHome', []);

twitterHome.controller('HomeController', ['$scope', '$rootScope', '$http', '$state', '$interval', 'Users', 'Credentials', 'LoginHelper', 'REST_API',
    function($scope, $rootScope, $http, $state, $interval, Users, Credentials, LoginHelper, REST_API) {

        var REFRESH_TOKEN_INTERVAL = 1 * 30 * 1000; // 30 seconds

        LoginHelper.isTokenSet(function(set) {
            if (!set) {
                $state.go('login');
                return;
            }

            LoginHelper.getUserRoleWithRegion(function(userRoleWithRegion) {
                if (userRoleWithRegion.role === 'ADMINISTRATOR') {
                    getAllUsers();
                } else if (userRoleWithRegion.role === 'REGION_ADMINISTRATOR') {
                    getUsersByRegion(userRoleWithRegion.region);
                }
            });
        });

        function getAllUsers() {
            Users.getUsersPromise().success(function(data) {
                $scope.users = data;
            });
        }

        function getUsersByRegion(region) {
            Users.getUsersPromise().success(function(data) {
                var usersWithSelectedRegion = [];
                angular.forEach(data, function(user, key) {
                    if (user.userProfile.eWallSubProfile.regionName === region) {
                        usersWithSelectedRegion.push(user);
                    }
                });
                $scope.users = usersWithSelectedRegion;
            });
        }

        var refreshingTokenPollTimer = $interval(function() {
            $http.get(REST_API.TOKEN_UPDATE).success(function(data) {
                LoginHelper.setToken(data.token);

            }).error(function(e) {
                console.log(e);
            });
        }, REFRESH_TOKEN_INTERVAL);

        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
            $interval.cancel(refreshingTokenPollTimer);
        });

        $scope.credentialsExist = false;

        $scope.checkIfCredentialsExist = function() {
            Credentials.getFlagCredentialsExistPromise($scope.user.username).then(function(exist) {
                if (exist.data) {
                    $scope.credentialsExist = true;
                } else {
                    $scope.credentialsExist = false;
                }
            });
        };

        $scope.authorizeUser = function() {
            var authWindow = window.open(REST_API.OPEN_AUTH);
            Credentials.getRequestTokenWithUrlPromise().then(function(twitterRequestTokenWithUrl) {
                authorizeAndSaveCredentialsInWindow(twitterRequestTokenWithUrl.data, authWindow);
            });
        };

        $scope.logoutUser = function() {
            LoginHelper.setToken(null);
            LoginHelper.setUserRoleWithRegion(null);
            $scope.user = null;
            $state.go('login');
        };

        function authorizeAndSaveCredentialsInWindow(twitterRequestTokenWithUrl, authWindow) {
            authWindow.location = twitterRequestTokenWithUrl.authUrl;

            var pollTimer = window.setInterval(function() {
                try {
                    var url = authWindow.document.URL;
                    if (url.indexOf(REST_API.REDIRECT_URI_TWITTER) != -1) {
                        window.clearInterval(pollTimer);
                        var verifier = getUrlQueryParameter(url, 'oauth_verifier');
                        var twitterRequestTokenWithVerifier = {
                            requestToken: twitterRequestTokenWithUrl.requestToken,
                            requestTokenSecret: twitterRequestTokenWithUrl.requestTokenSecret,
                            verifier: verifier
                        };

                        Credentials.postTwitterCredentialsForUsernamePromise($scope.user.username, twitterRequestTokenWithVerifier)
                            .success(function() {
                                authWindow.close();
                                $scope.user = null;
                                $scope.credentialsExist = false;
                                $scope.showSuccessMessage('Successfully authorized user and saved Twitter credentials to database.');
                            })
                            .error(function() {
                                authWindow.close();
                                $scope.showErrorMessage('Error while authorizing user and saving Twitter credentials to database.');
                            });
                    }
                } catch (e) {
                    // block frame warning
                }
            }, 500);
        }
    }
]);