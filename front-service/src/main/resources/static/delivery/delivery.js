angular.module('market-front').controller('deliveryController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/core/';

    $scope.imgPay = function () {
        var photo = {
            imgpath: "../img/karti-vse.jpg"
        };
        $scope.photo = photo,
        $scope.imgPay()
    }

});