package pexel.image.finder.portlet.configuration.definition;

import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;
import pexel.image.finder.portlet.configuration.PexelImageFinderPortletConfiguration;



public class PexelImageFinderPortletConfigurationBeanDeclaration implements ConfigurationBeanDeclaration {
    @Override
    public Class<?> getConfigurationBeanClass()
    {
        return PexelImageFinderPortletConfiguration.class;
    }
}
