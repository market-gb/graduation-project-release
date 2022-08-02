angular.module('market-front').controller('productController', function ($scope, $rootScope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/';

    $scope.showProductById = function (productId) {
            $http.get(contextPath + 'api/v1/products' + productId)
                  .then(function (response) {
                  $scope.showProduct = response.data;
            });
    }

    $scope.addToCart = function (productId) {
        $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/add/' + productId)
            .then(function (response) {
            });
    }

    $scope.showProductById();
});