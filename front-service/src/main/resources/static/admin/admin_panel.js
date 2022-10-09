angular.module('market-front').controller('adminController', function ($rootScope, $scope, $localStorage, $http) {

    const contextPath = 'http://localhost:5555/core/';
    $scope.userRole = {
        ROLE_ADMIN: "ROLE_ADMIN",
        ROLE_MANAGER: "ROLE_MANAGER",
        ROLE_USER: "ROLE_USER"
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

    $scope.newProduct = {title: '', description: '', price: '', group_id: ''};

    $scope.newCategory = {title: '', description: ''};

    $scope.newBanner = {title: ''};

    $rootScope.isUserHasAdminRole = function () {
        if (!!$localStorage.springWebUser){
            $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
            return $rootScope.listRoles.has('ROLE_ADMIN');
        }
        return false;
    };

    $rootScope.isUserHasManagerRole = function () {
        if (!!$localStorage.springWebUser){
            $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
            return $rootScope.listRoles.has('ROLE_MANAGER');
        }
        return false;
    };

    $rootScope.isUserHasUserRole = function () {
        if (!$rootScope.isUserLoggedIn()) {
            return false;
        }
        $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
        return $rootScope.listRoles.has('ROLE_USER');
    };

    $scope.getProductsAndCategories = function () {
        $scope.getAllCategories();
        $scope.loadProducts();
    };

    $scope.filesToUpload = null;

    $scope.fileInputChanged = function (element) {
        $scope.fileToUpload = element.files[0];
    };

    $scope.submitCreateNewProduct = function () {
        var fd = new FormData();
        fd.append("price", $scope.newProduct.price);
        fd.append("groupId", $scope.newProduct.group_id);
        fd.append("description", $scope.newProduct.description);
        fd.append("title", $scope.newProduct.title);
        fd.append('file', $scope.fileToUpload);
        $http.post(contextPath + 'api/v1/products', fd, {
            headers: {'Content-Type': undefined},
            data: fd,
            transformRequest: function (data) {
                return data;
            }
        }).then(function (response) {
            alert("Продукт добавлен");
            $scope.newProduct = null;
            $scope.loadProducts();
        });
    };

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
    $scope.bannerImageToUpload = null;

    $scope.bannerImageInputChanged = function (element) {
        $scope.bannerImageToUpload = element.files[0];
    };

    $scope.submitCreateNewBanner = function () {
        var fd = new FormData();
        fd.append("title", $scope.newBanner.title);
        fd.append('file', $scope.bannerImageToUpload);
        $http.post(contextPath + 'api/v1/banners', fd, {
            headers: {'Content-Type': undefined},
            data: fd,
            transformRequest: function (data) {
                return data;
            }
        }).then(function (response) {
            alert("Акция добавлена");
            $scope.newBanner = null;
            $scope.getAllBanners();
        });
    };

    $scope.getAllBanners = function () {
            $http.get(contextPath + 'api/v1/banners')
                .then(function (response) {
                    $scope.allBanners = response.data;
            });
    };

   $scope.deleteBanner = function (bannerId) {
               $http.delete(contextPath + 'api/v1/banners/' + bannerId)
                   .then(function (response) {
                       alert("Баннер удален");
                       $scope.getAllBanners();
                   });
   };

    // категории
    $scope.categoryImageToUpload = null;

    $scope.categoryImageInputChanged = function (element) {
        $scope.categoryImageToUpload = element.files[0];
    };

    $scope.submitCreateNewCategory = function () {
        var fd = new FormData();
        fd.append("description", $scope.newCategory.description);
        fd.append("title", $scope.newCategory.title);
        fd.append('file', $scope.categoryImageToUpload);
        $http.post(contextPath + 'api/v1/categories', fd, {
            headers: {'Content-Type': undefined},
            data: fd,
            transformRequest: function (data) {
                return data;
            }
        }).then(function (response) {
            alert("Категория добавлена");
            $scope.newProduct = null;
            $scope.loadProducts();
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

    // роли - видит только администратор
    $scope.getAllUsers = function (pageIndex = 1) {
           $http.get('http://localhost:5555/user/api/v1/users')
               .then(function (response) {
                   $scope.allUsers = response.data;
           });

        $http({
            url: 'http://localhost:5555/user/api/v1/users',
            method: 'GET',
            params: {
                p: pageIndex
            }
        }).then(function (response) {
            $scope.allUsers = response.data;

            let minPageIndex = pageIndex - 2;
            if (minPageIndex < 1) {
                minPageIndex = 1;
            }
            let maxPageIndex = pageIndex + 2;
            if (maxPageIndex > $scope.ProductsPage.totalPages) {
                maxPageIndex = $scope.ProductsPage.totalPages;
            }
            $scope.UserPaginationArray = $scope.generatePagesIndexes(minPageIndex, maxPageIndex);
        });
    };

    $scope.getUserById = function (userId){
        $http.get('http://localhost:5555/user/api/v1/users/' + userId)
            .then(function (response) {
                $scope.currentUser = response.data;
            });
    }

    $scope.changeUsersRole = function (roleNames, userId) {
        $http({
            url: 'http://localhost:5555/user/api/v1/users/roles/' + userId,
            method: 'PATCH',
            params: {roleNames: roleNames}
        }).then(function (response) {
                alert("Роль изменена");
                $scope.getUserById(userId);
                $scope.getAllUsers();
           });
    };

//    Пока закомментирую, а то поудаляют все
   $scope.deleteUser = function (userId) {
          $http.delete('http://localhost:5555/user/api/v1/users/' + userId)
              .then(function (response) {
                  alert("Пользователь удален");
                  $scope.getUserById(userId);
                  $scope.getAllUsers();
          });
   };
});