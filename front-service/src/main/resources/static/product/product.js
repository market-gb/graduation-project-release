angular.module('market-front').controller('productController', function ($scope, $http, $location, $localStorage, $routeParams) {

   $scope.loadProducts = function () {
            $http({
                url: 'http://localhost:5555/core/api/v1/products/' + $routeParams.productId,
                method: 'GET'
            }).then(function (response) {
                $scope.product = response.data;
            });
   };

    $scope.addToCart = function (productId) {
            $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/add/' + productId)
                .then(function (response) {
            });
    }

    $scope.loadProducts();
});