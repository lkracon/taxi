'use strict';

angular.module('yoApp').controller('TaxiCtrl', function($scope, $resource, $location, $filter, Host) {
  var Taxi = $resource(Host.backend + '/taxi/:id');
  var User = $resource(Host.backend + '/user/:id');


  $scope.taxis = Taxi.query();

  User.query({}, function(data) {
    $scope.drivers = $filter('filter')(data, {
      role: 'driver'
    });
  })

  $scope.addTaxi = function(user) {
    Taxi.save({}, user, function() {
      $scope.taxis = Taxi.query();
    });
  };

  $scope.goTo = function(id) {
    $location.path('/taxi/' + id);
  };

});