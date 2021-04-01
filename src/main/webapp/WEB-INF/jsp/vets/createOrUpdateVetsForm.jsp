<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<petclinic:layout pageName="vets">
    <h2> 
         <c:if test="${vet['new']}"><fmt:message key="new"/> <b> </b></c:if> <fmt:message key="vet"/> 
    </h2>
      <form:form modelAttribute="vet" class="form-horizontal" id="add-vets-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Nombre" name="firstName"/>
             <petclinic:inputField label="Apellido" name="lastName"/>
 			<petclinic:selectField label="Especialidad" names="${specialties}" name="specialties" size="5"/>
        </div>    
        
          <%-- <form:checkboxes items="${specialties}" path="specialties" name = "specialties"  itemLabel="specialties"/>  --%>    

         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vet['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addVet"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="saveVet"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div> 
    </form:form>
    
    <c:forEach var="spec" items="${specialties}" >
  		<p>${spec}</p>
	</c:forEach>
    
</petclinic:layout> 