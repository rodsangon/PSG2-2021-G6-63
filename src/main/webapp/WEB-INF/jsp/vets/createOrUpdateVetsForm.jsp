<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<petclinic:layout pageName="vets">
    <h2> 
         <c:if test="${vet['new']}">Nuevo</c:if> Veterinario 
    </h2>
      <form:form modelAttribute="vet" class="form-horizontal" id="add-vets-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="firstName" name="firstName"/>
             <petclinic:inputField label="lastName" name="lastName"/>
        </div>    
        
          <%-- <form:checkboxes items="${specialties}" path="specialties" name = "specialties"  itemLabel="specialties"/>  --%>    

         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vet['new']}">
                        <button class="btn btn-default" type="submit">Anadir veterinarios</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar veterinarios</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div> 
    </form:form>  
</petclinic:layout> 