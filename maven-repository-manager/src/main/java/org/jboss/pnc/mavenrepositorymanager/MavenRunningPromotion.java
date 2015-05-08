/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2015 Red Hat, Inc., and individual contributors
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
 *
 */

package org.jboss.pnc.mavenrepositorymanager;

import org.commonjava.aprox.client.core.Aprox;
import org.commonjava.aprox.client.core.AproxClientException;
import org.commonjava.aprox.model.core.Group;
import org.commonjava.aprox.model.core.StoreKey;
import org.commonjava.aprox.model.core.StoreType;
import org.jboss.pnc.spi.repositorymanager.RepositoryManagerException;
import org.jboss.pnc.spi.repositorymanager.model.CompletedRepositoryPromotion;
import org.jboss.pnc.spi.repositorymanager.model.RunningRepositoryPromotion;

import java.util.function.Consumer;

public class MavenRunningPromotion implements RunningRepositoryPromotion {

    private StoreType fromType;
    private String fromId;
    private String toId;
    private Aprox aprox;

    public MavenRunningPromotion(StoreType fromType, String fromId, String toId, Aprox aprox) {
        this.fromType = fromType;
        this.fromId = fromId;
        this.toId = toId;
        this.aprox = aprox;
    }

    /**
     * Trigger the repository promotion configured for this instance, and send the result to the appropriate consumer.
     * 
     * @param onComplete The consumer which will handle non-error results
     * @param onError Handles errors
     */
    @Override
    public void monitor(Consumer<CompletedRepositoryPromotion> onComplete, Consumer<Exception> onError) {
        try {
            if (!aprox.stores().exists(fromType, fromId)) {
                throw new RepositoryManagerException("No such %s repository: %s", fromType.singularEndpointName(), fromId);
            }

            Group recordSetGroup = aprox.stores().load(StoreType.group, toId, Group.class);
            if (recordSetGroup == null) {
                throw new RepositoryManagerException("No such group: %s", toId);
            }

            recordSetGroup.addConstituent(new StoreKey(fromType, fromId));

            boolean result = aprox.stores().update(recordSetGroup,
                    "Promoting " + fromType.singularEndpointName() + " repository : " + fromId + " to group: " + toId);

            onComplete.accept(new MavenCompletedPromotion(result));

        } catch (AproxClientException | RepositoryManagerException e) {
            onError.accept(e);
        }
    }

}
