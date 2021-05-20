package pexel.image.finder.portlet.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import pexel.image.finder.constants.PexelImageFinderPortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + PexelImageFinderPortletKeys.PEXELIMAGEFINDER,
                "mvc.command.name=" + PexelImageFinderPortletKeys.IMPORT_COMMAND
        },
        service = MVCActionCommand.class
)
public class PexelImageImportMVCActionCommand extends BaseMVCActionCommand {
    // TODO create configuration
    String APIKEY = "563492ad6f9170000100000146bcf9a8b889402691549f6fa9b32af7";


    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        System.out.println("the import is on...");

        HttpUriRequest request = RequestBuilder
                .get("https://api.pexels.com/v1/photos/2014422")
                .addHeader("Authorization", PexelImageFinderPortletKeys.APIKEY)
                .build();

        //hideDefaultSuccessMessage(actionRequest);
    }

    private String getRequest(HttpUriRequest request) {
        HttpClient client = HttpClientBuilder.create().build();

        HttpResponse response = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static final Log _log = LogFactoryUtil.getLog(PexelImageImportMVCActionCommand.class);
}