angular.module('market-front').controller('adminController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/core/';

    $scope.submitCreateNewProduct = function () {
        $http.post(contextPath + 'api/v1/products', $scope.newProduct)
            .then(function (response) {
                alert("Продукт добавлен");
                $location.path('/store');
            });
    };

    $scope.submitCreateNewBanner = function () {
        $http.post(contextPath + '/api/v1/banners', $scope.newBanner)
            .then(function (response) {
                alert("Акция добавлен");
            });
    };

    $scope.submitCreateNewCategory = function () {
        $http.post(contextPath + '/api/v1/category', $scope.newCategory)
            .then(function (response) {
                alert("Категория добавлена");
            });
    };
});