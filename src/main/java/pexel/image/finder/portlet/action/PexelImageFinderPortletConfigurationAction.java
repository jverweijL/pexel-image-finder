package pexel.image.finder.portlet.action;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import org.osgi.service.component.annotations.Component;
import pexel.image.finder.constants.PexelImageFinderPortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + PexelImageFinderPortletKeys.PEXELIMAGEFINDER
        },
        service = ConfigurationAction.class
)
public class PexelImageFinderPortletConfigurationAction extends DefaultConfigurationAction {
    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception
    {
        super.processAction(portletConfig,actionRequest,actionResponse);
    }
}
