package org.jboss.pnc.integration.deployments;

import org.jboss.arquillian.container.spi.event.container.BeforeDeploy;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class ArquillianExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder builder) {

        builder.observer(IgnoreWFStartupServiceFailure.class);
    }

    public static class IgnoreWFStartupServiceFailure {

        public void ignore(@Observes EventContext<BeforeDeploy> ec) {
            try {
                ec.proceed();
            }
            catch(NullPointerException e) {
                // Error in WF ServerSetupObserver, assumes an active ClassContext
            }
        }
    }
}

