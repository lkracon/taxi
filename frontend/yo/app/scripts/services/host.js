'use strict';

angular.module('yoApp')
  .factory('Host', function () {
    return {
      backend: "http://172.20.10.5:8080/taxi/api"
    };
  });
