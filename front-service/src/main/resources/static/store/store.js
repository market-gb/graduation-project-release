angular.module('market-front').controller('storeController', function ($scope, $rootScope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/';
    $scope.category = ["Аксессуары", "Телевизоры", "Компьютеры", "Офис и сеть", "Для кухни", "Для дома", "Строительство", "Для дачи", "Для отдыха"];

    $scope.loadProducts = function (pageIndex = 1) {
        $http({
            url: contextPath + 'api/v1/products',
            method: 'GET',
            params: {
                p: pageIndex,
                title_part: $scope.filter ? $scope.filter.title_part : null,
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null,
                category_title: $scope.filter ? $scope.filter.category_title : null,
                category_id: $rootScope.categoryId
            }
        }).then(function (response) {
            $scope.ProductsPage = response.data;

            let minPageIndex = pageIndex - 2;
            if (minPageIndex < 1) {
                minPageIndex = 1;
            }
            let maxPageIndex = pageIndex + 2;
            if (maxPageIndex > $scope.ProductsPage.totalPages) {
                maxPageIndex = $scope.ProductsPage.totalPages;
            }
            // $scope.paginationArray = $scope.generatePagesIndexes(1, $scope.ProductsPage.totalPages);
            $scope.PaginationArray = $scope.generatePagesIndexes(minPageIndex, maxPageIndex);
        });
    };

    $scope.generatePagesIndexes = function (startPage, endPage) {
        let arr = [];
        for (let i = startPage; i < endPage + 1; i++) {
            arr.push(i);
        }
        return arr;
    }

    $scope.goToProduct = function (productId) {
                $location.path('/product/' + productId);
    }


    $scope.addToCart = function (productId) {
        $http.get('http://localhost:5555/cart/api/v1/carts/' + $localStorage.springWebGuestCartId + '/add/' + productId)
            .then(function (response) {
            });
    }

    $scope.loadProducts();
});