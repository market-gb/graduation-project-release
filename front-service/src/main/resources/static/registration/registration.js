angular.module('market-front').controller('regController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/auth/';

    $scope.tryToReg = function () {
        $http.post('http://localhost:5555/auth/new_user_auth', $scope.user, $scope.email, $scope.password, $scope.adress)
            .then(function successCallback(response) {
                $localStorage.springWebUser = {
                    username: $scope.user.username,
                    password: $scope.user.password,
                    email: $scope.user.email,
                    adress: $scope.user.adress
                };
                $scope.user.username = null;
                $scope.user.email = null;
                $scope.user.password = null;
                $scope.user.adress = null;

                $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/merge')
                    .then(function successCallback(response) {
                    });

                $location.path('/');

            }, function errorCallback(response) {
            });

    };
});