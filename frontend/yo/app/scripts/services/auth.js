'use strict';

angular.module('yoApp')
  .service('Auth', function($http, $cookieStore, Host) {
    this.user = $cookieStore.get('user');
    this.token = $cookieStore.get('token');

    var self = this;

    this.login = function(user, success, error) {
      return $http.post(Host.backend + '/user/auth', {}, {
        params: user,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Accept': 'application/json'
        }
      }).success(function(data) {
        self.user = data.user;
        self.token = data.token;
        $cookieStore.put('user', self.user);
        $cookieStore.put('token', self.token);
        if (success) {
          success();
        }
      }).error(error);
    }

  });