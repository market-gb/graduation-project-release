angular.module('market-front').controller('adminController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/core/';
    let selectedRole = [];
    let selectedStatus = [];

    // товары
    $scope.submitCreateNewProduct = function () {
        $http.post(contextPath + 'api/v1/products', $scope.newProduct)
            .then(function (response) {
                alert("Продукт добавлен");
            });
    };

//    Пока закомментирую, а то поудаляют все
//     $scope.deleteProduct = function (productId) {
//            $http.delete(contextPath + 'api/v1/products' + productId)
//                .then(function (response) {
//                    alert("Продукт удален");
//                });
//    };

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
    $scope.submitCreateNewBanner = function () {
        $http.post(contextPath + 'api/v1/banners', $scope.newBanner)
            .then(function (response) {
                alert("Акция добавлен");
            });
    };

    $scope.getAllBanner = function () {
            $http.get(contextPath + 'api/v1/banners')
                .then(function (response) {
                    $scope.allBanners = response.data;
            });
    };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteBanner = function (bannerId) {
//                $http.delete(contextPath + 'api/v1/banners/' + bannerId)
//                    .then(function (response) {
//                        alert("Баннер удален");
//                    });
//    };

    // категории
    $scope.submitCreateNewCategory = function () {
        $http.post(contextPath + 'api/v1/categories', $scope.newCategory)
            .then(function (response) {
                alert("Категория добавлена");
            });
    };

    $scope.getAllCategories = function () {
           $http.get(contextPath + 'api/v1/categories')
               .then(function (response) {
                   $scope.allCategories = response.data;
           });
    };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteCategory = function (categoryId) {
//                $http.delete(contextPath + 'api/v1/categories/' + categoryId)
//                    .then(function (response) {
//                        alert("Категория удалена");
//                    });
//    };

    // заказы
    $scope.getAllOrders = function () {
           $http.get(contextPath + 'api/v1/orders')
               .then(function (response) {
                   $scope.allOrders = response.data;
           });
    };

    $scope.getOrderById = function (orderId) {
               $http.get(contextPath + 'api/v1/orders/' + orderId)
                   .then(function (response) {
                       $scope.orderById = response.data;
               });
    };

//    Пока нет функционала для обработки статуса заказа, но как-то так
//    $scope.changeStatus = function (orderId, statusId) {
//            $http.patch(contextPath + 'api/v1/orders/' + orderId + statusId)
//                .then(function (response) {
//                    alert("Статус изменен");
//            });
//    };


//    Пока закомментирую, а то поудаляют все
//    $scope.deleteOrder = function (orderId) {
//           $http.delete(contextPath + 'api/v1/orders/' + orderId)
//               .then(function (response) {
//                   alert("Ордер удален");
//           });
//    };
    $scope.getAllStatus = function () {
          $http.get('http://localhost:8188/orderStatus')
              .then(function (response) {
                  $scope.allOrderStatus = response.data;
          });
    };

    $scope.clickOrderStatus = function (statusId, selected) {
            var idx = selectedStatus.indexOf(statusId);
            if (idx > -1) {
                selectedStatus.splice(idx, 1);
            } else {
                selectedStatus.push(statusId);
            }
    }

    $rootScope.isAdminLoggedIn = function () {
        if ($localStorage.springWebUser == 'admin') {
            return true;
        } else {
            return false;
        }
    };

    $rootScope.isManagerLoggedIn = function () {
            if ($localStorage.springWebUser == 'manager') {
                return true;
            } else {
                return false;
            }
    };

     // роли - видит только администратор
     $scope.getAllUsers = function () {
            $http.get('http://localhost:8188/users')
                .then(function (response) {
                    $scope.allUsers = response.data;
            });
     };

     $scope.getUserById = function (userId) {
                 $http.get('http://localhost:8188/users' + userId)
                     .then(function (response) {
                         $scope.userById = response.data;
                 });
          };

     $scope.getAllRoles = function () {
           $http.get('http://localhost:8188/roles')
               .then(function (response) {
                   $scope.allRoles = response.data;
           });
     };

     $scope.clickRole = function (roleId, selected) {
             var idx = selectedRole.indexOf(roleId);
             if (idx > -1) {
                 selectedRole.splice(idx, 1);
             } else {
                 selectedRole.push(roleId);
             }
     }

//     Пока нет функционала для обработки ролей пользователя, но как-то так
//     $scope.changeUsersRole = function (userId, roleId) {
//            $http.post('http://localhost:8188/users' + userId + roleId)
//                .then(function (response) {
//                 alert("Роль изменена");
//            });
//     };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteUser = function (userId) {
//           $http.delete('http://localhost:8188/users' + userId)
//               .then(function (response) {
//                   alert("Пользователь удален");
//           });
//    };

    $scope.getAllBanner();
    $scope.getAllCategories();
    $scope.getAllOrders();
    $scope.loadProducts();
});