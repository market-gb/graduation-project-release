angular.module('market-front').controller('productController', function ($scope, $routeParams, $rootScope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/';

   $scope.productId = $routeParams.productId;

   $scope.showProduct = function (productId) {
         $http({
             url: contextPath + 'api/v1/products' + productId,
             method: 'GET'
         }).then(function (response) {
             $scope.Product = response.data;
         });
      };


    $scope.addToCart = function (productId) {
            $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/add/' + productId)
                .then(function (response) {
                });
    }

    $scope.showProduct();
});