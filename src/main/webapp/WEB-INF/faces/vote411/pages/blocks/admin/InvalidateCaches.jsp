<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="column-form faces list caches">
    <div class="hd">
        <h2>Caches</h2>
    </div>
    <div class="bd">
        <table>
        <tr>
            <td>    
            <h3> Information About Cache Types:</h3>
                <ul>
                    <li><strong>PvrDataCsv and PvrDataStatus</strong> - used for storing satus and data during file generation for Pending Voter Registration</li>
                    <li><strong>article, articleList, tagList</strong> - for caching information from FactCheck API. Articles and tag for categorizing the articles are stored here</li>
                    <li><strong>electionStates, electionsAll, electionsOfState</strong> - for caching elections from Local Elections API</li>
                    <li><strong>pdfKeys</strong> - storing information used during PDF generation process</li>
                    <li><strong>secondaryContent</strong> - for caching additional content from Drupal site for home page</li>
                </ul>
            </td>
        </tr>
            <form action="<c:url value="/admin/InvalidateCaches.htm"/>" method="post">
                <fieldset>
                    <c:forEach items="${cacheNames}" var="cacheName">
                        <tr class="region-name">
                            <td>
                                <label><input type="checkbox" value="${cacheName}" name="cacheName"> ${cacheName}</label>
                            </td>
                        </tr>
                    </c:forEach>
                </fieldset>
                <tr class="region-name">
                    <td><br/><label><input type="submit" value="Invalidate Caches" name="Invalidate Caches"></label></td>
                </tr>
            </form>
        </table>
    </div>
    <div class="ft"></div>
</div>