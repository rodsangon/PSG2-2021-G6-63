<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<petclinic:layout pageName="adoption">
	<form:form modelAttribute="adoption" class="form-horizontal">
	<c:out value="${adoptant.id}"></c:out>
		<input type="hidden" name="originalOwnerId"
			value="${originalOwner.id}" />
			<input type="hidden" name="adoptantId"
			value="${adoptant.id}" />
		<div class="form-group has-feedback">
			<petclinic:inputField label="Descripción" name="description" />
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">
					<fmt:message key="adoptPet" />
				</button>
			</div>
		</div>
	</form:form>
</petclinic:layout>
