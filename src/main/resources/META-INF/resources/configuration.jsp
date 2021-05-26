<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ taglib prefix="aui" uri="http://liferay.com/tld/aui" %>
<%@ taglib prefix="liferay-ddm" uri="http://liferay.com/tld/ddm" %>

<%@ include file="/init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="<%=true%>" var="configurationActionURL"/>
<liferay-portlet:renderURL portletConfiguration="<%=true%>" var="configurationRenderURL"/>

<aui:form action="<%=configurationActionURL%>" method="post" name="fm" class="form">
    <aui:input name="<%= Constants.CMD%>" type="hidden" value="<%= Constants.UPDATE%>"/>
    <aui:input name="redirect" type="hidden" value="<%=configurationRenderURL%>"/>

    <div class="portlet-configuration-body-content">
        <div class="container-fluid">
            <aui:fieldset  markupView="lexicon">
                <aui:input name="preferences--apiKey--" label="Define apiKey" value="${apiKey}"></aui:input>
            </aui:fieldset>
            <aui:fieldset  markupView="lexicon">
                <aui:input name="preferences--path--" label="Define path  in Document and Media Library" value="${path}"></aui:input>
            </aui:fieldset>
        </div>
    </div>

    <aui:button-row>
        <aui:button cssClass="btn-lg" type="submit"/>
    </aui:button-row>
</aui:form>

<!-- https://www.masteringliferay.com/members/learning-portal/videos/lesson/-/play/making-your-portlets-configurable -->