<%@ page import="java.util.HashMap" %>
<%@ taglib prefix="clay" uri="http://liferay.com/tld/clay" %>
<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL name="/pexel/import" var="imageimport" />
<aui:form action="<%= imageimport %>" method="post" name="fmimport">
	<aui:input name="id" type="hidden" value="-1"/>
</aui:form>

<liferay-portlet:actionURL name="/pexel/search" var="search" />

<div class=pexel-searchbar">
	<aui:form action="<%= search %>" method="post" name="fm">
		<aui:fieldset cssClass="search-bar mb-3">
			<div class="input-group search-bar-simple">
				<div class="input-group-item search-bar-keywords-input-wrapper">
					<input class="form-control input-group-inset input-group-inset-after search-bar-keywords-input"
						   name="<portlet:namespace/>psearch"
						   id="<portlet:namespace/>psearch"
						   placeholder="Search Pexels..."
						   title="Search"
						   type="text"
					/>
					<div class="input-group-inset-item input-group-inset-item-after">
						<clay:button
								aria-label='Submit'
								displayType="unstyled"
								icon="search"
								type="submit"
						/>
					</div>
				</div>
			</div>
		</aui:fieldset>
	</aui:form>
</div>
<div class="pexel-searchresults">

	<c:forEach items="${result}" var="photo">
		<img src="${photo.getValue()}" data-id="${photo.getKey()}"/>
	</c:forEach>

</div>

<a href="https://www.pexels.com">Photos provided by Pexels</a>

<a href="https://www.pexels.com">
	<img src="https://images.pexels.com/lib/api/pexels.png" />
</a>

