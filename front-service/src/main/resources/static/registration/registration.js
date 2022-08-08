angular.module('market-front').controller('regController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/user/';

    $scope.tryToReg = function () {
        $http.post(contextPath + 'new_user_auth', $scope.user, $scope.email, $scope.password, $scope.address)
            .then(function successCallback(response) {
                $localStorage.springWebUser = {
                    username: $scope.user.username,
                    password: $scope.user.password,
                    email: $scope.user.email,
                    address: $scope.user.address
                };
                $scope.user.username = null;
                $scope.user.email = null;
                $scope.user.password = null;
                $scope.user.address = null;

                $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/merge')
                    .then(function successCallback(response) {
                    });

                $location.path('#!/');

            }, function errorCallback(response) {
        });
    };

    $rootScope.tryToLogout = function () {
            $scope.clearUser();
            $scope.user = null;
            $location.path('#!/');
    };

    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.springWebUser) {
            return true;
        } else {
            return false;
        }
    };
});