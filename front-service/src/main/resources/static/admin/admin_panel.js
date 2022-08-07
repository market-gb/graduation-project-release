angular.module('market-front').controller('adminController', function ($rootScope, $scope, $localStorage, $http) {
    const contextPath = 'http://localhost:5555/core/';
    $scope.userRole = {
        ROLE_ADMIN: "АДМИНИСТРАТОР",
        ROLE_MANAGER: "МЕНЕДЖЕР",
        ROLE_USER: "ПОЛЬЗОВАТЕЛЬ"
    };

    $scope.orderStatus = {
        CREATED: "CREATED",
        PAID: "PAID",
        NOT_PAID: "NOT_PAID",
        CANCELLED: "CANCELLED",
        COMPLETED: "COMPLETED",
        IN_PROCESS: "IN_PROCESS",
        SHIPPED: "SHIPPED"
    };

    $scope.category = ["Аксессуары", "Телевизоры", "Компьютеры", "Офис и сеть", "Для кухни", "Для дома", "Строительство", "Для дачи", "Для отдыха"];

    $scope.newProduct = {title: '', description: '', price: 0, pathname: '', group_id: []};

    $scope.newCategory = {title: '', description: '', pathname: ''};

        $scope.groupId = 0;

    $scope.imagePathname = null;

    $rootScope.isUserHasAdminRole = function () {
        if (!$rootScope.isUserLoggedIn()) {
            return false;
        }
        $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
        return $rootScope.listRoles.has('ROLE_ADMIN');
    };

    $scope.getProductsAndCategories = function () {
        $scope.getAllCategories();
        $scope.loadProducts();
    };

    // товары
    $scope.submitCreateNewProduct = function () {
        $scope.newProduct.pathname = 'img/products/' + document.getElementById(
            "newProductImage").files[0].name;
        $scope.newProduct.group_id[0] = $scope.groupId;
        $http.post(contextPath + 'api/v1/products', $scope.newProduct)
            .then(function (response) {
                alert("Продукт добавлен");
                $scope.newProduct = null;
                $scope.loadProducts();
            });
    };

//    Пока закомментирую, а то поудаляют все
    $scope.deleteProduct = function (productId) {
           $http.delete(contextPath + 'api/v1/products/' + productId)
               .then(function (response) {
                   alert("Продукт удален");
                   $scope.loadProducts();
               });
   };

    $scope.loadProducts = function (pageIndex = 1) {
        $http({
            url: contextPath + 'api/v1/products',
            method: 'GET',
            params: {
                p: pageIndex,
                title_part: $scope.filter ? $scope.filter.title_part : null,
                category_title: $scope.filter ? $scope.filter.category_title : null
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

    // баннеры
    // $scope.submitCreateNewBanner = function () {
    //     $http.post(contextPath + 'api/v1/banners', $scope.newBanner)
    //         .then(function (response) {
    //             alert("Акция добавлен");
    //         });
    // };

    // $scope.getAllBanner = function () {
    //         $http.get(contextPath + 'api/v1/banners')
    //             .then(function (response) {
    //                 $scope.allBanners = response.data;
    //         });
    // };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteBanner = function (bannerId) {
//                $http.delete(contextPath + 'api/v1/banners/' + bannerId)
//                    .then(function (response) {
//                        alert("Баннер удален");
//                    });
//    };

    // категории
    $scope.submitCreateNewCategory = function () {
        $scope.newCategory.pathname = 'img/category/' + document.getElementById(
            "newCategoryImage").files[0].name;
        $http.post(contextPath + 'api/v1/categories', $scope.newCategory)
            .then(function (response) {
                alert("Категория добавлена");
                $scope.newCategory = null;
                $scope.getAllCategories();
            });
    };

    $scope.getAllCategories = function (pageIndex = 1) {
        $http({
            url: contextPath + 'api/v1/categories',
            method: 'GET',
            params: {
                p: pageIndex
            }
        }).then(function (response) {
            $scope.allCategories = response.data;
            let minPageIndex = pageIndex - 2;
            if (minPageIndex < 1) {
                minPageIndex = 1;
            }
            let maxPageIndex = pageIndex + 2;
            if (maxPageIndex > $scope.allCategories.totalPages) {
                maxPageIndex = $scope.allCategories.totalPages;
            }
            $scope.CategoryPaginationArray = $scope.generatePagesIndexes(minPageIndex, maxPageIndex);

        });
    };

//    Пока закомментирую, а то поудаляют все
    $scope.deleteCategory = function (categoryId) {
        $http.delete(contextPath + 'api/v1/categories/' + categoryId)
            .then(function (response) {
                alert("Категория удалена");
                $scope.getAllCategories();
            });
    };

    // заказы
    $scope.getAllOrders = function () {
        $http.get(contextPath + 'api/v1/orders/all')
            .then(function (response) {
                $scope.allOrders = response.data;
            });
    };

    // $scope.getOrderById = function (orderId) {
    //            $http.get(contextPath + 'api/v1/orders/' + orderId)
    //                .then(function (response) {
    //                    $scope.orderById = response.data;
    //            });
    // };


    $scope.changeStatus = function (orderStatus, orderId) {
        $http({
            url: contextPath + 'api/v1/orders/' + orderId,
            method: 'PATCH',
            params: {orderStatus: orderStatus}
        }).then(function (response) {
            alert("Статус изменен");
            $scope.getAllOrders();
        });
    };


//    Пока закомментирую, а то поудаляют все
    $scope.deleteOrder = function (orderId) {
        $http.delete(contextPath + 'api/v1/orders/' + orderId)
            .then(function (response) {
                alert("Ордер удален");
                $scope.getAllOrders();
            });
    };

    // $scope.getAllStatus = function () {
    //       $http.get(contextPath + 'api/v1/orders/statuses')
    //           .then(function (response) {
    //               $scope.allOrderStatus = response.data;
    //       });
    // };

    // $scope.clickOrderStatus = function (statusId, selected) {
    //         var idx = selectedStatus.indexOf(statusId);
    //         if (idx > -1) {
    //             selectedStatus.splice(idx, 1);
    //         } else {
    //             selectedStatus.push(statusId);
    //         }
    // }

    // роли - видит только администратор
    // $scope.getAllUsers = function () {
    //        $http.get('http://localhost:5555/user/users')
    //            .then(function (response) {
    //                $scope.allUsers = response.data;
    //        });
    // };
    //
    // $scope.getUserById = function (userId) {
    //             $http.get('http://localhost:5555/user/users' + userId)
    //                 .then(function (response) {
    //                     $scope.userById = response.data;
    //             });
    //      };

    // $scope.getAllRoles = function () {
    //       $http.get('http://localhost:5555/user/roles')
    //           .then(function (response) {
    //               $scope.allRoles = response.data;
    //       });
    // };

    // $scope.clickRole = function (roleId, selected) {
    //         var idx = selectedRole.indexOf(roleId);
    //         if (idx > -1) {
    //             selectedRole.splice(idx, 1);
    //         } else {
    //             selectedRole.push(roleId);
    //         }
    // }

    // $scope.changeUsersRole = function (userRole, userId) {
    //        $http.post('http://localhost:5555/user/users' + x + userId)
    //            .then(function (response) {
    //             alert("Роль изменена");
    //        });
    // };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteUser = function (userId) {
//           $http.delete('http://localhost:5555/user/users' + userId)
//               .then(function (response) {
//                   alert("Пользователь удален");
//           });
//    };
});