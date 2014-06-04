'use strict';

angular.module('yoApp').controller('TaxiSingleCtrl', function($scope, $resource, $stateParams, $filter, $interval, Host) {
  var Taxi = $resource(Host.backend + '/taxi/:id');
  var User = $resource(Host.backend + '/user/:id');

  User.query({}, function(data) {
    $scope.drivers = $filter('filter')(data, {
      role: 'driver'
    });
  })

  $scope.taxiMarker;

  $scope.taxi = Taxi.get({
    id: $stateParams.id
  }, function(taxi) {
    $scope.taxiMarker = new google.maps.Marker({
      position: new google.maps.LatLng(taxi.lastLocation.latitude, taxi.lastLocation.longitude),
      map: map,
      title: taxi.user.firstName + " "+taxi.user.lastName
    });

    $interval(function() {
      Taxi.get({
        id: $stateParams.id
      }, function(taxi) {
        $scope.taxiMarker.setPosition(new google.maps.LatLng(taxi.lastLocation.latitude, taxi.lastLocation.longitude));
        map.setCenter(new google.maps.LatLng(taxi.lastLocation.latitude, taxi.lastLocation.longitude))
      });
    }, 1000)


  });

  var map;
  function initialize() {
    var mapOptions = {
      center: new google.maps.LatLng(50.062208, 19.938759),
      zoom: 12
    };
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
  }

  initialize();


});