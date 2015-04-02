package org.jboss.pnc.integration.deployments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.pnc.integration.BuildTest;
import org.jboss.pnc.rest.endpoint.BuildConfigurationEndpoint;
import org.jboss.pnc.rest.endpoint.BuildConfigurationSetEndpoint;
import org.jboss.pnc.rest.endpoint.BuildRecordEndpoint;
import org.jboss.pnc.rest.endpoint.BuildRecordSetEndpoint;
import org.jboss.pnc.rest.endpoint.EnvironmentEndpoint;
import org.jboss.pnc.rest.endpoint.LicenseEndpoint;
import org.jboss.pnc.rest.provider.BuildConfigurationProvider;
import org.jboss.pnc.rest.provider.BuildConfigurationSetProvider;
import org.jboss.pnc.rest.provider.BuildRecordProvider;
import org.jboss.pnc.rest.provider.BuildRecordSetProvider;
import org.jboss.pnc.rest.provider.EnvironmentProvider;
import org.jboss.pnc.rest.provider.LicenseProvider;
import org.jboss.pnc.rest.restmodel.BuildConfigurationRest;
import org.jboss.pnc.rest.restmodel.BuildConfigurationSetRest;
import org.jboss.pnc.rest.restmodel.BuildRecordRest;
import org.jboss.pnc.rest.restmodel.BuildRecordSetRest;
import org.jboss.pnc.rest.restmodel.EnvironmentRest;
import org.jboss.pnc.rest.restmodel.LicenseRest;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public abstract class RestDeployment {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Deployment(testable = false)
    public static EnterpriseArchive deploy() {
        EnterpriseArchive enterpriseArchive = Deployments.baseEar();

        // Explicitly adding these classes again to make development from IDE simpler in case of recompilation.
        WebArchive restWar = enterpriseArchive.getAsType(WebArchive.class, "/pnc-rest.war");
        restWar.addClass(BuildConfigurationEndpoint.class);
        restWar.addClass(BuildConfigurationProvider.class);
        restWar.addClass(BuildConfigurationRest.class);
        restWar.addClass(BuildConfigurationSetEndpoint.class);
        restWar.addClass(BuildConfigurationSetProvider.class);
        restWar.addClass(BuildConfigurationSetRest.class);
        restWar.addClass(BuildRecordEndpoint.class);
        restWar.addClass(BuildRecordProvider.class);
        restWar.addClass(BuildRecordRest.class);
        restWar.addClass(BuildRecordSetProvider.class);
        restWar.addClass(BuildRecordSetEndpoint.class);
        restWar.addClass(BuildRecordSetRest.class);
        restWar.addClass(BuildTest.class);
        restWar.addClass(EnvironmentEndpoint.class);
        restWar.addClass(EnvironmentProvider.class);
        restWar.addClass(EnvironmentRest.class);
        restWar.addClass(LicenseEndpoint.class);
        restWar.addClass(LicenseProvider.class);
        restWar.addClass(LicenseRest.class);

        logger.info(enterpriseArchive.toString(true));

        return enterpriseArchive;
    }
}
