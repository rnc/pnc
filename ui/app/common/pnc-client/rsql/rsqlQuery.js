/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014-2020 Red Hat, Inc., and individual contributors
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

  var module = angular.module('pnc.common.pnc-client.rsql');

  /**
   * @ngdoc service
   * @kind function
   * @name pnc.common.pnc-client.rsql:rsqlQuery
   * @description
   * Provides a fluent API for creating RSQL queries
   *
   * @param {boolean} isOperatorFirst
   * Set true whether it should be possible to start rsql query with operator.
   * This option is useful when you need to concatenate multiple rsql query strings.
   * 
   * @example
   * rsqlQuery().where('buildConfiguration.name').like('jboss%').and().where('id').gt(4).end();
   * rsqlQuery(true).and().where('key').eq('value').end();
   *
   * @author Alex Creasy
   */
  module.factory('rsqlQuery', [
    'context',
    function (context) {
      return function rsqlQuery(isOperatorFirst) {
        var ctx = context(isOperatorFirst);
        return ctx.next();
      };
    }
  ]);

})();
