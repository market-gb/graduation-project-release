angular.module('market-front').controller('adminController', function ($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://localhost:5555/core/';
    $scope.userRole = {
     ROLE_ADMIN : "АДМИНИСТРАТОР",
     ROLE_MANAGER : "МЕНЕДЖЕР",
     ROLE_USER : "ПОЛЬЗОВАТЕЛЬ"
    };

    $scope.orderStatus = {
        CREATED : "СОЗДАН",
        PAID : "ОПЛАЧЕН",
        NOT_PAID : "НЕ ОПЛАЧЕН",
        CANCELLED : "ОТМЕНЕН",
        COMPLETED : "СОБРАН",
        IN_PROCESS : "В ПРОЦЕССЕ",
        SHIPPED : "ДОСТАВЛЕН"
    };

    $scope.category = ["Аксессуары", "Телевизоры", "Компьютеры", "Офис и сеть", "Для кухни", "Для дома", "Строительство", "Для дачи", "Для отдыха"];

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


    $scope.changeStatus = function (orderStatus, orderId) {
        $http({
            url: contextPath + 'api/v1/orders/' + orderId,
            method: 'PATCH',
            data: orderStatus
        }).then(function (response) {
            alert("Статус изменен");
        });
    };


//    Пока закомментирую, а то поудаляют все
//    $scope.deleteOrder = function (orderId) {
//           $http.delete(contextPath + 'api/v1/orders/' + orderId)
//               .then(function (response) {
//                   alert("Ордер удален");
//           });
//    };

    $scope.getAllStatus = function () {
          $http.get(contextPath + 'api/v1/orders/statuses')
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

    $scope.isAdminLoggedIn = function () {
        return !!$localStorage.springWebUser && $localStorage.springWebUser.username === 'admin';
    };

    $scope.isManagerLoggedIn = function () {
        return !!$localStorage.springWebUser && $localStorage.springWebUser.username === 'manager';
    };

     // роли - видит только администратор
     $scope.getAllUsers = function () {
            $http.get('http://localhost:5555/user/users')
                .then(function (response) {
                    $scope.allUsers = response.data;
            });
     };

     $scope.getUserById = function (userId) {
                 $http.get('http://localhost:5555/user/users' + userId)
                     .then(function (response) {
                         $scope.userById = response.data;
                 });
          };

     $scope.getAllRoles = function () {
           $http.get('http://localhost:5555/user/roles')
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

     $scope.changeUsersRole = function (userRole, userId) {
            $http.post('http://localhost:5555/user/users' + userRole + userId)
                .then(function (response) {
                 alert("Роль изменена");
            });
     };

//    Пока закомментирую, а то поудаляют все
//    $scope.deleteUser = function (userId) {
//           $http.delete('http://localhost:5555/user/users' + userId)
//               .then(function (response) {
//                   alert("Пользователь удален");
//           });
//    };

    $scope.getAllBanner();
    $scope.getAllCategories();
    $scope.getAllOrders();
    $scope.loadProducts();
});