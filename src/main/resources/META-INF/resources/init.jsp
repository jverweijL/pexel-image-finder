<%@ page import="pexel.image.finder.portlet.configuration.PexelImageFinderPortletConfiguration" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    PexelImageFinderPortletConfiguration  portletInstanceConfiguration = portletDisplay.getPortletInstanceConfiguration(PexelImageFinderPortletConfiguration.class);
    String apiKey = portletInstanceConfiguration.apiKey();
    pageContext.setAttribute("apiKey",apiKey);
    String path = portletInstanceConfiguration.path();
    pageContext.setAttribute("path",path);
%>