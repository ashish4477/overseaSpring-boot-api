<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<form action="" method="post" class="bc-form" id="search-form">
	<input name="_m" value="core" type="hidden">
	<input name="searchtype" value="knowledgebase" type="hidden">
	<input name="_a" value="searchclient" type="hidden">

	<fieldset>
		<label class="one-line text search-field">
			<span class="label">Search Terms</span><span class="field">
				<span class="wrapper">
					<input name="" id="search-terms" type="text" value="<c:out value="${searchTerms}"/>" />
				</span>
			</span>

		</label>

		<label class="one-line select category-field">
			<span class="label">Search Category</span>
			<span class="field">
				<span class="wrapper">
					<select id="seach-type-select">
						<option value="website">OVF Web Site</option>
						<option value="eod">EOD Database</option>
						<option value="helpdesk">Voter Help Desk</option>
					</select>
				</span>
			</span>
		</label>

	</fieldset>
	<div class="form-controls">
		<span class="primary-control">
			<input type="image" src="<c:url value="/img/buttons/go-button.gif"/>" />
		</span>
	</div>
</form>