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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + PexelImageFinderPortletKeys.PEXELIMAGEFINDER,
                "mvc.command.name=" + PexelImageFinderPortletKeys.SEARCH_COMMAND
        },
        service = MVCActionCommand.class
)
public class PexelImageFinderMVCActionCommand extends BaseMVCActionCommand {



    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        _log.debug("the search is on...");

        String query = URLEncoder.encode(ParamUtil.getString(actionRequest, "psearch"),"UTF-8");
        int offset = 0;

        ObjectMapper mapper = new ObjectMapper();

        HttpUriRequest request = RequestBuilder
                .get("https://api.pexels.com/v1/search?query=" + query)
                .addHeader("Authorization", PexelImageFinderPortletKeys.APIKEY)
                .build();

        try {
            JsonNode jsonResult = mapper.readTree(getRequest(request));

            HashMap<Long,String> result = new HashMap<Long, String>();
            for (JsonNode photo : jsonResult.get("photos")) {
                result.put(photo.get("id").asLong(-1),photo.get("src").get("small").asText(""));
            }


            actionRequest.setAttribute("result",result);

           _log.debug("the result is " + jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

        _log.debug("the search is done");

        hideDefaultSuccessMessage(actionRequest);
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

    private static final Log _log = LogFactoryUtil.getLog(PexelImageFinderMVCActionCommand.class);
}