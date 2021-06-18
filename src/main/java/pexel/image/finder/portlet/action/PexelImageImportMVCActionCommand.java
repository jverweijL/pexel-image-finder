package pexel.image.finder.portlet.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import pexel.image.finder.constants.PexelImageFinderPortletKeys;
import pexel.image.finder.portlet.configuration.PexelImageFinderPortletConfiguration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component(
        configurationPid = "pexel.image.finder.portlet.configuration.PexelImageFinderPortletConfiguration",
        immediate = true,
        property = {
                "javax.portlet.name=" + PexelImageFinderPortletKeys.PEXELIMAGEFINDER,
                "mvc.command.name=" + PexelImageFinderPortletKeys.IMPORT_COMMAND
        },
        service = MVCActionCommand.class
)
public class PexelImageImportMVCActionCommand extends BaseMVCActionCommand {

    private volatile PexelImageFinderPortletConfiguration _configuration;

    @Activate
    @Modified
    protected void activate(Map<String,Object> properties)
    {
        _configuration = ConfigurableUtil.createConfigurable(PexelImageFinderPortletConfiguration.class,properties);
    }

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (themeDisplay.isSignedIn()) {

            String apiKey = "";
            String path = "";
            PortletPreferences preferences = actionRequest.getPreferences();
            if (Validator.isNotNull(preferences)) {
                apiKey = GetterUtil.getString(preferences.getValue("apiKey", "APIKEY_NOT_SET"));
                path = GetterUtil.getString(preferences.getValue("path", "/images/pexels"));
            }

            // first get info on remote asset
            String id = URLEncoder.encode(ParamUtil.getString(actionRequest, "id"), "UTF-8");

            HttpUriRequest request = RequestBuilder
                    .get("https://api.pexels.com/v1/photos/" + id)
                    .addHeader("Authorization", apiKey)
                    .build();

            String imgTitle = "";
            String imgUrl = "";

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResult = mapper.readTree(getRequest(request));

                imgTitle = jsonResult.get("url").asText().replaceFirst("/$", "").replaceFirst(".*/", "");
                imgUrl = jsonResult.get("src").get("large").asText();

                System.out.println(imgTitle);
                System.out.println(imgUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // get the actual image
            request = RequestBuilder
                    .get(imgUrl)
                    .build();

            // first create required folder structure
            ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), actionRequest);

            long folderID = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

            String[] dirs = path.substring(1).split("/");
            System.out.println(dirs.length);
            for (String directory : dirs) {
                System.out.println("Create folder: " + directory);
                Folder folder = null;
                try {
                    folder = DLAppServiceUtil.getFolder(themeDisplay.getScopeGroupId(), folderID, directory);
                } catch (PortalException x) {
                    // guess it doesn't exist, try to create one
                    folder = DLAppServiceUtil.addFolder(themeDisplay.getScopeGroupId(), folderID, directory, "auto generated by pexels", serviceContext);
                }
                folderID = folder.getFolderId();
                System.out.println(folderID);
            }

            // see if file exists already if not we add it otherwise we just show message file already exists
            boolean fileExists = false;
            try {
                // see if file already exists
                FileEntry f = DLAppServiceUtil.getFileEntry(themeDisplay.getScopeGroupId(), folderID, imgTitle);
                fileExists = true;
                SessionErrors.add(actionRequest, "entryExists");
            } catch (PortalException pe) {

            }

            if (!fileExists) {
                // upload file to folder
                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                        try (InputStream inputStream = entity.getContent()) {
                            DLAppServiceUtil.addFileEntry(themeDisplay.getScopeGroupId(), folderID, "123.jpeg", response.getEntity().getContentType().getValue(),
                                    imgTitle, "auto import pexels", "", inputStream, response.getEntity().getContentLength(), serviceContext);
                            SessionMessages.add(actionRequest, "entryAdded");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            SessionErrors.add(actionRequest, "signIn");
        }
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