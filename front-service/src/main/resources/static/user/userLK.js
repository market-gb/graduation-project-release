angular.module('market-front').controller('userController', function ($rootScope, $scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/user/';

    $scope.loadUser = function (id) {
            $http({
                url: 'http://localhost:5555/user/api/v1/user' + id,
                method: 'GET'
            }).then(function (response) {
                $scope.user = response.data;
            });
    };

});