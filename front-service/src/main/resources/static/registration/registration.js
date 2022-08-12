angular.module('market-front').controller('regController', function ($rootScope, $scope, $http) {
    const contextPath = 'http://localhost:5555/user/';

    $scope.newUser = {username: '', password: '', email: '', passwordConfirm: ''};

    $scope.tryToReg = function () {

        $http.post(contextPath + 'new_user_auth', $scope.newUser)
            .then(function successCallback(response) {

                $scope.newUser.username = null;
                $scope.newUser.email = null;
                $scope.newUser.password = null;
                $scope.newUser.passwordConfirm = null;

                $location.path('#!/');

            }, function errorCallback(response) {
        });
    };

    $scope.tryToLogout = function () {
            $scope.clearUser();
            $scope.newUser = null;
            $location.path('#!/');
    };

    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        return !!$localStorage.springWebUser;
    };
});