'use strict';

angular.module('yoApp').controller('MainCtrl', function($scope, $resource, $stateParams, $filter, $interval, $http,
  Host) {
  var Taxi = $resource(Host.backend + '/taxi/:id');
  var User = $resource(Host.backend + '/user/:id');

  User.query({}, function(data) {
    $scope.drivers = $filter('filter')(data, {
      role: 'driver'
    });
  });

  $scope.addr;

  $scope.getLocation = function(val) {
    return $http.get('http://maps.google.com/maps/api/geocode/json', {
      params: {
        address: val,
        sensor: false
      }
    }).then(function(res) {
      var addresses = [];
      angular.forEach(res.data.results, function(item) {
        addresses.push({
          name: item.formatted_address,
          coords: item.geometry.location
        });
      });
      return addresses;
    });
  };


  $scope.taxiMarkers = [];

  $interval(function() {
    $scope.taxis = Taxi.query({}, function(taxis) {

      angular.forEach(taxis, function(taxi) {
        var markerExist = false;
        angular.forEach($scope.taxiMarkers, function(marker) {
          if (marker.id == taxi.id) {
            markerExist = true;
            marker.marker.setPosition(new google.maps.LatLng(taxi.lastLocation.latitude, taxi.lastLocation.longitude));
          }
        });
        if (!markerExist) {
          $scope.taxiMarkers.push({
            id: taxi.id,
            marker: new google.maps.Marker({
              position: new google.maps.LatLng(taxi.lastLocation.latitude, taxi.lastLocation.longitude),
              map: map,
              title: '[' + taxi.id + '] ' + taxi.user.firstName + ' ' + taxi.user.lastName
            })
          });
        }
      });
    });
  }, 1000);

  var Nearest = $resource(Host.backend + '/taxi/nearest');
  $interval(function() {
    if(!$scope.addr || !$scope.addr.coords || !$scope.addr.coords.lat || !$scope.addr.coords.lng){
      return;
    }
    Nearest.query({
      latitude: $scope.addr.coords.lat,
      longitude: $scope.addr.coords.lng
    }, function(data) {
      $scope.nearest = data;
    });
  }, 1000);

  $scope.nearest;

  $scope.hilightTaxi = function(nearest) {

    angular.forEach($scope.taxiMarkers, function(marker) {
      if (marker.id == nearest.taxi.id) {
        if (marker.marker.getAnimation() != null) {
          marker.marker.setAnimation(null);
        } else {
          marker.marker.setAnimation(google.maps.Animation.BOUNCE);
        }
      } else {
        marker.marker.setAnimation(null);
      }
    });
  };

  $scope.format = function(val) {
    return Number(val / 1000).toFixed(2);
  };

  var map;

  function initialize() {
    var mapOptions = {
      center: new google.maps.LatLng(50.062208, 19.938759),
      zoom: 12
    };
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
  }

  initialize();

  var addrMarker = null;
  $scope.$watch("addr.coords", function(val) {
    if (!val) {
      return;
    }

    if (addrMarker) {
      addrMarker.setPosition(new google.maps.LatLng(val.lat, val.lng));
    } else {
      addrMarker = new google.maps.Marker({
        position: new google.maps.LatLng(val.lat, val.lng),
        map: map,
        title: $scope.addr.name,
        icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
      });
    }

  });

});