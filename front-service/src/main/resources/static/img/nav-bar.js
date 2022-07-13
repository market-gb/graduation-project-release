angular.module('market-front').controller('welcomeController', function ($scope, $http) {
    const contextPath = 'http://localhost:5555/core/';

    angular.module('market-front').directive('footer', function(){
        return {
            templateUrl : '../footer/footer.html',
        }
    });

    app.controller('NavbarController', function($scope, $location) {
    $scope.getClass = function(path) {
        if ($location.path().substr(0, path.length) == path) {
            return true
        } else {
            return false;
        }
    }
});



});