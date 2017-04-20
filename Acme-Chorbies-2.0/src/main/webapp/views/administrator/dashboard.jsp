<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>



<h3>
	<spring:message code="administrator.listingWithTheNumberOfChorbiesPerCountryAndCity" />
</h3>

<jstl:if test="${listingWithTheNumberOfChorbiesPerCountryAndCity.size() != 0 }">
<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="listingWithTheNumberOfChorbiesPerCountryAndCity" requestURI="${requestURI}" id="row">

	<spring:message code="administrator.country" var="country" />
	<display:column value="${row[0]}" title="${country}" />

	<spring:message code="administrator.city" var="city" />
	<display:column value="${row[1]}" title="${city}" />
	
	<spring:message code="administrator.chorbies" var="chorbies" />
	<display:column value="${row[2]}" title="${chorbies}" />

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.minimumMaximumAverageAgesOfTheChorbies" />
</h3>

<jstl:if test="${minimumMaximumAverageAgesOfTheChorbies.size() != 0 }">
<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="minimumMaximumAverageAgesOfTheChorbies" requestURI="${requestURI}" id="row">

	<spring:message code="administrator.min" var="min" />
	<display:column value="${row[0]}" title="${min}" />

	<spring:message code="administrator.avg" var="avg" />
	<display:column value="${row[1]}" title="${avg}" />
	
	<spring:message code="administrator.max" var="max" />
	<display:column value="${row[2]}" title="${max}" />

</display:table>
</jstl:if>


<h3>
	<spring:message
		code="administrator.ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard" id="row">

	<spring:message
		code="administrator.ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard"
		var="ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard" />
	<display:column
		title="${ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard}"
		sortable="false">

		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>


<h3>
	<spring:message
		code="administrator.ratiosOfChorbiesWhoSearchActivities" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="ratiosOfChorbiesWhoSearchActivities" id="row">

	<spring:message
		code="administrator.ratiosOfChorbiesWhoSearchActivities"
		var="ratiosOfChorbiesWhoSearchActivities" />
	<display:column title="${ratiosOfChorbiesWhoSearchActivities}"
		sortable="false">

		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>


<h3>
	<spring:message
		code="administrator.ratiosOfChorbiesWhoSearchFriendship" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="ratiosOfChorbiesWhoSearchFriendship" id="row">

	<spring:message
		code="administrator.ratiosOfChorbiesWhoSearchFriendship"
		var="ratiosOfChorbiesWhoSearchFriendship" />
	<display:column title="${ratiosOfChorbiesWhoSearchFriendship}"
		sortable="false">

		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>


<h3>
	<spring:message code="administrator.ratiosOfChorbiesWhoSearchLove" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="ratiosOfChorbiesWhoSearchLove" id="row">

	<spring:message code="administrator.ratiosOfChorbiesWhoSearchLove"
		var="ratiosOfChorbiesWhoSearchLove" />
	<display:column title="${ratiosOfChorbiesWhoSearchLove}"
		sortable="false">

		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>


<h3>
	<spring:message code="administrator.listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot" />
</h3>

<jstl:if test="${listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot.size() != 0 }">
<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot" requestURI="${requestURI}" id="row">

	<spring:message code="administrator.chorbi.name" var="name" />
	<display:column value="${row[0]}" title="${name}" />

	<spring:message code="administrator.chorbi.surName" var="surName" />
	<display:column value="${row[1]}" title="${surName}" />
	
	<spring:message code="administrator.count" var="count" />
	<display:column value="${row[2]}" title="${count}" />

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.minOfLikesPerChorbie" />
</h3>

<jstl:if test="${minOfLikesPerChorbie.size() != 0 }">
<display:table class="displaytag" keepStatus="true"
	name="minOfLikesPerChorbie" id="row">

	<spring:message code="administrator.minOfLikesPerChorbie"
		var="minOfLikesPerChorbie" />
	<display:column title="${minOfLikesPerChorbie}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.maxOfLikesPerChorbie" />
</h3>

<jstl:if test="${maxOfLikesPerChorbie.size() != 0 }">
<display:table class="displaytag" keepStatus="true"
	name="maxOfLikesPerChorbie" id="row">

	<spring:message code="administrator.maxOfLikesPerChorbie"
		var="maxOfLikesPerChorbie" />
	<display:column title="${maxOfLikesPerChorbie}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.averageLikesPerChorbi" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="averageLikesPerChorbi" id="row">

	<spring:message code="administrator.averageLikesPerChorbi"
		var="averageLikesPerChorbi" />
	<display:column title="${averageLikesPerChorbi}" sortable="false">

		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>

</display:table>


<h3>
	<spring:message code="administrator.theChorbiesWhoHaveGotMoreChirps" />
</h3>

<jstl:if test="${theChorbiesWhoHaveGotMoreChirps.size() != 0 }">
<display:table class="displaytag" keepStatus="true"
	name="theChorbiesWhoHaveGotMoreChirps" id="row">

	<spring:message code="administrator.chorbi.name" var="nameActor" />
	<display:column property="name" title="${nameActor}" sortable="false" />

	<spring:message code="administrator.chorbi.surName" var="surNameActor" />
	<display:column property="surName" title="${surNameActor}"
		sortable="false" />

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.theChorbiesWhoHaveSentMoreChirps" />
</h3>

<jstl:if test="${theChorbiesWhoHaveSentMoreChirps.size() != 0 }">
<display:table class="displaytag" keepStatus="true"
	name="theChorbiesWhoHaveSentMoreChirps" id="row">

	<spring:message code="administrator.chorbi.name" var="nameActor" />
	<display:column property="name" title="${nameActor}" sortable="false" />

	<spring:message code="administrator.chorbi.surName" var="surNameActor" />
	<display:column property="surName" title="${surNameActor}"
		sortable="false" />

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.minAvgMaxChirpsReceived" />
</h3>

<jstl:if test="${minAvgMaxChirpsReceived.size() != 0 }">
<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="minAvgMaxChirpsReceived" requestURI="${requestURI}" id="row">

	<spring:message code="administrator.min" var="min" />
	<display:column value="${row[0]}" title="${min}" />

	<spring:message code="administrator.avg" var="avg" />
	<display:column value="${row[1]}" title="${avg}" />
	
	<spring:message code="administrator.max" var="max" />
	<display:column value="${row[2]}" title="${max}" />

</display:table>
</jstl:if>


<h3>
	<spring:message code="administrator.minAvgMaxChirpsSent" />
</h3>

<jstl:if test="${minAvgMaxChirpsSent.size() != 0 }">
<display:table pagesize="10" class="displaytag" keepStatus="true"
	name="minAvgMaxChirpsSent" requestURI="${requestURI}" id="row">

	<spring:message code="administrator.min" var="min" />
	<display:column value="${row[0]}" title="${min}" />

	<spring:message code="administrator.avg" var="avg" />
	<display:column value="${row[1]}" title="${avg}" />
	
	<spring:message code="administrator.max" var="max" />
	<display:column value="${row[2]}" title="${max}" />

</display:table>
</jstl:if>