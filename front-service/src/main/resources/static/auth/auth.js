angular.module('market-front').controller('authController', function ($rootScope, $scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/user/';

    $scope.tryToAuth = function () {
        $http.post(contextPath + 'auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.springWebUser = {username: $scope.user.username, token: response.data.token, listRoles: response.data.list};
                    $rootScope.currentUserName = $scope.user.username;
                    $scope.user.username = null;
                    $scope.user.password = null;

                    $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/merge')
                        .then(function successCallback(response) {
                        });

                    $location.path('#!/');
                }
            }, function errorCallback(response) {
            });
    };

    $rootScope.tryToLogout = function () {
        $rootScope.listRoles.clear();
        $scope.clearUser();
        $scope.user = null;
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        return !!$localStorage.springWebUser;
    };

});