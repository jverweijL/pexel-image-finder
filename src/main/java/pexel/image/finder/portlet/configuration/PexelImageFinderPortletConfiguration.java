package pexel.image.finder.portlet.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
        category = "liferay-custom",
        scope = ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE
)
@Meta.OCD(
        id = "pexel.image.finder.portlet.configuration.PexelImageFinderPortletConfiguration"
)
public interface PexelImageFinderPortletConfiguration {

    @Meta.AD(deflt = "0", name = "api-key", required = false)
    public String apiKey();
}
