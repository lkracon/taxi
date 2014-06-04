'use strict';

angular.module('yoApp').controller('LoginCtrl', function($scope, $location, Auth) {

  $scope.user;

  $scope.login = function() {
    Auth.login($scope.user, function() {
      $location.path('/');
    })
  }

});