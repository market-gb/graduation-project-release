angular.module('market-front').controller('welcomeController', function ($scope, $rootScope, $http, $location, $localStorage, $routeParams) {
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

    $rootScope.showProductsByCategory = function (categoryTitle) {
       $location.path('/store/' + categoryTitle);
    };


    $scope.loadCategory();
});
