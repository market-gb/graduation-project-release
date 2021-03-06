angular.module('market-front').controller('welcomeController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/core/';

    $scope.loadCategory = function (pageIndex = 1) {
        $http({
            url: contextPath + 'api/v1/category',
            method: 'GET'
        }).then(function (response) {
            $scope.Category = response.data;
        });
    };

    $scope.showProductsByCategory = function (categoryId) {
        $http({
            url: contextPath + 'api/v1/category/' + categoryId,
            method: 'GET'
        }).then(function (response) {
            $scope.ProductsByCategory = response.data;
        });
    }

    $scope.showProductsByCategory();
    $scope.loadCategory();
});
