angular.module('market-front').controller('welcomeController', function ($rootScope, $scope, $http, $location) {
    const contextPath = 'http://localhost:5555/core/';
    $rootScope.categoryId = null;

    $scope.loadCategory = function (pageIndex = 1) {
        $http({
            url: contextPath + 'api/v1/categories',
            method: 'GET'
        }).then(function (response) {
            $scope.Category = response.data;
        });
    };

//    $scope.showProductsByCategory = function (categoryId) {
//        $http({
//            url: contextPath + '/api/v1/products/category/' + categoryId,
//            method: 'GET'
//        }).then(function (response) {
//            $scope.ProductsByCategory = response.data;
//            $location.path('/' + $scope.ProductsByCategory);
//        });
//    }

    $rootScope.showProductsByCategoryId = function (categoryId) {
        $rootScope.categoryId = categoryId;
        $location.path('/store');
    };

//    $scope.showProductsByCategory();
    $scope.loadCategory();
});
