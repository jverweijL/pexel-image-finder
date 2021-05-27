<%@ page import="java.util.HashMap" %>
<%@ taglib prefix="clay" uri="http://liferay.com/tld/clay" %>
<%@ include file="/init.jsp" %>

<aui:script>
	window.importImage = function(id){
		document.getElementById('<portlet:namespace/>id').value=id;
		document.getElementById("<portlet:namespace/>fmimport").submit();
	}
</aui:script>

<liferay-portlet:actionURL name="/pexel/import" var="imageimport" />
<aui:form action="<%= imageimport %>" method="post" name="fmimport">
	<aui:input name="id" type="hidden" value="-1"/>
</aui:form>

<liferay-portlet:actionURL name="/pexel/search" var="search" />

<liferay-ui:success key="entryAdded" message="entry-added" />
<liferay-ui:error  key="entryExists"  message="entry-exists" />
<liferay-ui:error  key="signIn"  message="signin" />

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
		<a href="javascript:void(0)" onclick="importImage('${photo.getKey()}');">
			<img src="${photo.getValue()}" data-id="${photo.getKey()}"/>
		</a>
	</c:forEach>

</div>

<a href="https://www.pexels.com">Photos provided by Pexels</a><br/>

<a href="https://www.pexels.com">
	<img class="w-25" src="https://images.pexels.com/lib/api/pexels.png" />
</a>

