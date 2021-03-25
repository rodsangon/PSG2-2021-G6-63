<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
     <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
    <h2>New booking</h2>
    
    <b>Pet</b>
    <table class="table table-striped">
    	<thead>
        <tr>
        	<th>Name</th>
            <th>Birth Date</th>
            <th>Type</th>
            <th>Owner</th>
        </tr>
        </thead>
        <tr>
        	<td><c:out value="${booking.pet.name}"/></td>
            <td><petclinic:localDate date="${booking.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
            <td><c:out value="${booking.pet.type.name}"/></td>
            <td><c:out value="${booking.pet.owner.firstName} ${pet.owner.lastName}"/></td>
        </tr>
    </table>
    
    <form:form modelAttribute="booking" class="form-horizontal">
    	<div class="form-group has-feedback">   
    		<petclinic:inputField label="Details" name="details"/>
     		<petclinic:inputField label="Check In" name="checkIn"/>
     		<petclinic:inputField label="Check Out" name="checkOut"/>
     		
     	</div>
     	
     	<div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${booking.pet.id}"/>
                    <button class="btn btn-default" type="submit">Reserve</button>
                </div>
            </div>  	
    </form:form>
    
    
            <br/>
        <b>Previous Bookings</b>
        <table class="table table-striped">
            <tr>
                <th>Check In</th>
                <th>Check Out</th>
                <th>Details</th>
            </tr>
            <c:forEach var="booking" items="${booking.pet.bookings}">
                <c:if test="${!booking['new']}">
                    <tr>
                        <td><petclinic:localDate date="${booking.checkIn}" pattern="yyyy/MM/dd"/></td>
                        <td><petclinic:localDate date="${booking.checkOut}" pattern="yyyy/MM/dd"/></td>
                        <td><c:out value="${booking.details}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
		
     	

 
    </jsp:body>
</petclinic:layout>
