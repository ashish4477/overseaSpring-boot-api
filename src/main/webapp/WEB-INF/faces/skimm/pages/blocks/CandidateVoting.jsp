<%--
	Created by IntelliJ IDEA.
	User: Leo
	Date: Oct 10, 2007
	Time: 4:09:39 PM
	To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="voter-account-page" class="voter-account-page page-form body-content content column wide-content">
<div class="col-xs-12">
      <h2>Key Votes Cast By ${candidate.firstName} ${candidate.lastName}</h2> <br/>
  <form action="CandidateVoting.htm" method="post">
  <input type="hidden" name="candidateId" value="${candidate.id}">
  <c:if test="${not empty candidate}">
    <div class="row representatives">
            <div class="col-xs-12 col-sm-3">
              <img src="${candidate.photo}" class="pull-right">
            </div>
                <div class="col-xs-12 col-sm-8">
                  <h3> ${candidate.firstName} ${candidate.middleName} ${candidate.lastName}</h3>
                  <h4> ${candidate.office.name} - ${candidate.office.district}, ${candidate.office.parties}</h4>
                  <div class="row">
                      <div class="col-xs-12 col-sm-8">
                       <select name="categoryId" class="form-control">
                        <c:forEach var="category" items="${votesCategories}">
                            <c:set var="selected" value=""/>
                            <c:if test="${param.categoryId eq category.id}">
                            <c:set var="selected" value="selected='selected'"/>
                            </c:if>
                            <option value="${category.id}" ${selected}>${category.name}</option>
                        </c:forEach>
                        </select>
                        </div>
                         <div class="col-xs-12 col-sm-4">
                         <select name="year" class="form-control">
                            <option value="">All years</option>
                            <c:forEach items="${years}" var="year">
                                <c:set var="selected" value=""/>
                                <c:if test="${param.year eq year}">
                                <c:set var="selected" value="selected='selected'"/>
                                </c:if>
                                <option value="${year}" ${selected}>${year}</option>
                            </c:forEach>
                         </select>
                         </div>
                    </div>
                  <input type="submit" value="Search">
                 </div>
            </div>
  </c:if>

</form>
  <c:if test="${not empty bills}">
    <div class="content">
        <c:forEach var="bill" items="${bills}">
            <p class="bill">voted <strong>${bill.vote}</strong> (${bill.stage}) - ${bill.billNumber} - ${bill.title}</p>
        </c:forEach>
    </div>
    </c:if>

</div>
 </div>
  <p>&nbsp;</p>
     <script>
    jQuery(function() {
     jQuery('.bill strong:contains("Nay")').css('color', '#7D0501');
     jQuery('.bill strong:contains("Yea")').css('color', '#177E4C');
      } );
  </script>
 </div>








