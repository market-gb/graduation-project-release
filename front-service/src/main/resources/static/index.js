(function () {
    angular
        .module('market-front', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/store/:categoryId?', {
                            templateUrl: 'store/store.html',
                            controller: 'storeController'
            })
            .when('/product/:productId', {
                 templateUrl: 'product/product.html',
                 controller: 'productController'
                 })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/user', {
                templateUrl: 'user/userLK.html',
                controller: 'userController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/order_pay/:orderId', {
                templateUrl: 'order_pay/order_pay.html',
                controller: 'orderPayController'
            })
            .when('/delivery', {
                templateUrl: 'delivery/delivery.html',
                controller: 'deliveryController'
            })
            .when('/politics', {
                templateUrl: 'politics/politics.html',
                controller: 'politicsController'
            })
            .when('/payment', {
                templateUrl: 'payment/payment.html',
                controller: 'paymentController'
            })
            .when('/contacts', {
                templateUrl: 'contacts/contacts.html',
                controller: 'contactsController'
            })
            .when('/auth', {
                templateUrl: 'auth/auth.html',
                controller: 'authController'
            })
            .when('/user', {
                 templateUrl: 'user/userLK.html',
                 controller: 'userController'
            })
            .when('/registration', {
                templateUrl: 'registration/registration.html',
                controller: 'regController'
            })
            .when('/admin', {
                templateUrl: 'admin/admin_panel.html',
                controller: 'adminController'
            })
            .otherwise({
                redirectTo: '/'
            });
     }


    function run($rootScope, $http, $localStorage, $routeParams) {
        if ($localStorage.springWebUser) {
            try {
                let jwt = $localStorage.springWebUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.springWebUser;
                    $http.defaults.headers.common.Authorization = '';
                }
            } catch (e) {
            }

            if ($localStorage.springWebUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.springWebUser.token;
            }
        }
        if (!$localStorage.springWebGuestCartId) {
            $http.get('http://localhost:5555/cart/api/v1/carts/generate')
                .then(function successCallback(response) {
                    $localStorage.springWebGuestCartId = response.data.value;
                });
        }
    }
})();

angular.module('market-front').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage, $routeParams) {
    $rootScope.listRoles = new Set();
    if ($localStorage.springWebUser){
        $rootScope.currentUserName = $localStorage.springWebUser.username;
    }

    $scope.tryToAuth = function () {
        $location.path('/auth');
    };

    $rootScope.tryToLogout = function () {
        $rootScope.listRoles.clear();
        $scope.clearUser();
        $scope.user = null;
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        return !!$localStorage.springWebUser;
    };

    $rootScope.isUserHasAdminRole = function () {
        if (!$rootScope.isUserLoggedIn()){
            return false;
        }
        $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
        return $rootScope.listRoles.has('ROLE_ADMIN');
    };

    $rootScope.isUserHasManagerRole = function () {
        if (!$rootScope.isUserLoggedIn()){
            return false;
        }
        $localStorage.springWebUser.listRoles.forEach($rootScope.listRoles.add, $rootScope.listRoles);
        return $rootScope.listRoles.has('ROLE_MANAGER');
    };
});
