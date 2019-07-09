/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014-2019 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  'use strict';

  var module = angular.module('pnc.common.pnc-client.resources');

  module.value('SCM_REPOSITORY_PATH', '/scm-repositories/:id');

  /**
   *
   */
  module.factory('ScmRepositoryResource', [
    '$resource',
    '$http',
    'restConfig',
    'SCM_REPOSITORY_PATH',
    function($resource, $http, restConfig, SCM_REPOSITORY_PATH) {
      var ENDPOINT = restConfig.getPncRestUrl() + SCM_REPOSITORY_PATH;

      var resource = $resource(ENDPOINT, {
        id: '@id'
      }, {
        query: {
          method: 'GET',
          isPaged: true,
        },
        /**
         * Get SCM Repository by id.
         */
        get: {
          method: 'GET'
        },
        update: {
          method: 'PUT',
          transformRequest: function(data) {
            // convert empty string value to null, see NCL-3992
            if (data.externalUrl === '') {
              data.externalUrl = null;
            }
            return angular.toJson(data);
          }
        },
        search: {
          method: 'GET',
          isPaged: true,
          url: restConfig.getPncUrl() + '/scm-repositories/search-by-scm-url'
        },
        match : {
          method: 'GET',
          isPaged: true,
          url: restConfig.getPncUrl() + '/scm-repositories/match-by-scm-url'
        }
      });

      resource.autoCreateRepoConfig = function (options) {
        var dto = {
          scmUrl: options.url,
          preBuildSyncEnabled: options.preBuildSync,
          buildConfigurationRest: options.buildConfiguration
        };

        return $http.post(restConfig.getPncUrl() + '/bpm/tasks/start-repository-configuration-creation-url-auto', dto);
      };

      resource.prototype.getName = function () {
        return this.internalUrl.split('/').splice(3).join('/');
      };

      return resource;
    }

  ]);

})();