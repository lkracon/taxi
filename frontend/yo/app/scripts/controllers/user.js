'use strict';

angular.module('yoApp').controller('UserCtrl', function($scope, $resource, $location, Host) {
  var User = $resource(Host.backend + '/user/:id');

  $scope.roles = [
    {name:'Customer', val:'customer'},
    {name:'Driver', val:'driver'},
    {name:'Admin', val:'admin'}
  ];

  $scope.users = User.query();

  $scope.addUser = function(user) {
    User.save({}, user, function(){
      $scope.users = User.query();
    });
  };

  $scope.goTo = function(id) {
    $location.path('/user/' + id);
  };

});