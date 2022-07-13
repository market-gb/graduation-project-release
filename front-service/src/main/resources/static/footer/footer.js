angular.module('market-front').controller('footerController', function ($scope) {
    const contextPath = 'http://localhost:5555/';
}).directive('footer',function () {
    return {
        templateUrl: 'footer.html',
        replace: true
    }
});
