<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script src="<c:url value="/scripts/codemirror/lib/codemirror.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/scripts/codemirror/lib/codemirror.css"/>">
<script src="<c:url value="/scripts/codemirror/mode/xml/xml.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/scripts/codemirror/theme/default.css"/>">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/admin.css"/>" />


<div class="column-form faces instructions">
    <div class="hd">
        <h2>Edit Face Flow Instruction</h2>
    </div>
    <div class="bd">
        <c:url value="/admin/EditFaceFlowInstruction.htm" var="actionUrl"/>
        <form:form commandName="instruction" action="${actionUrl}" method="POST">
            <form:hidden path="id"/>
            <input type="hidden" name="configId" value="${instruction.faceConfig.id}"/>
        <a class="btn" href="<c:url value='/admin/EditFaceConfig.htm'><c:param name="configId" value="${instruction.faceConfig.id}"/></c:url>">Go Back to the Face Config</a>
        <table>
            <tr class="region-name">
                <th>Face</th>
                <td>${instruction.faceConfig.urlPath}</td>
            </tr>
            <tr class="region-name">
                <th>Flow</th>
                <td>
                    <c:choose>
                        <c:when test="${instruction.id > 0}">${instruction.flowTypeName}</c:when>
                        <c:otherwise>
                            <form:select path="flowTypeName" items="${types}"/>
                        </c:otherwise>
                    </c:choose>
                    <form:errors path="flowTypeName"/>
                </td>
            </tr>
            <tr  class="region-name">
                <th>Instruction<sup>*</sup></th>
                <td>
                    <form:textarea path="text"  id="flowFaceText" cols="60" rows="20"/>
                    <form:errors path="text"/>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td colspan="2">
                    <input type="submit" name="save" value="Save Instruction"/>&nbsp;
                </td>
            </tr>
        </table>
        </form:form>
        <div id="instruction_help">
            <p>Following tags are allowed for using:</p>
            <table cellpadding="4" cellspacing="4">
                <tr>
                    <th>Tag name</th>
                    <th>Meaning</th>
                    <th>Attributes</th>
                </tr>
                <tr>
                    <td><strong>&lt;p&gt;</strong></td>
                    <td>Paragraph</td>
                    <td>margin, margin-top, margin-left, margin-bottom, margin-right,
                        padding, padding-top, padding-left, padding-bottom, padding-right,
                        border-top, border-left, border-bottom, border-right,
                        text-align</td>
                </tr>
                <tr>
                    <td><strong>&lt;ul&gt;,&lt;ol&gt;</strong></td>
                    <td>Unsorted and sorted list</td>
                    <td>margin-top, margin-left, type</td>
                </tr>
                <tr>
                    <td><strong>&lt;li&gt;</strong></td>
                    <td>List element</td>
                    <td>margin-top</td>
                </tr>
                <tr>
                    <td><strong>&lt;font&gt;</strong></td>
                    <td>Font</td>
                    <td>size, family, line-height</td>
                </tr>
                <tr>
                    <td><strong>&lt;b&gt;</strong></td>
                    <td>Bold text</td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>&lt;i&gt;</strong></td>
                    <td>Italic text</td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>&lt;u&gt;</strong></td>
                    <td>Underlined text</td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>&lt;a href=""&gt;</strong></td>
                    <td>External link</td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>&lt;span&gt;</strong></td>
                    <td>Inline part of text</td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>&lt;nb&gt;</strong></td>
                    <td>Normal (non-bold) text</td>
                    <td></td>
                </tr>

            </table>
            <p>There are also variables available for use: </p>
            <p>User's variables:</p>
            <p><em>
                ufVotingAddress, ufVotingCity, ufVotingState, ufVotingZipCode, ufVotingRegion,<br/>

                ufCurrentAddress, ufCurrentCity, ufCurrentState, ufCurrentZipCode, ufCurrentCountry,<br/>
                ufForwardingAddress, ufForwardingCity, ufForwardingState, ufForwardingZipCode, ufForwardingCountry,<br/>
                ufMailingAddress, ufMailingCity, ufMailingState, ufMailingZipCode, ufMailingCountry,<br/>

                ufPhone, ufEmail, ufFirstName, ufLastName", ufMiddleName, ufSuffix, ufPreviousName,
                ufVoterType, ufBirthMonth, ufBirthDate, ufBirthYear

                ufParty, ufRace, ufEthnicity, ufGender,
                ufBallotPref, ufVoterHistory
            </em></p>

            <p>Other variables:<br/>

                <em>f1_2_f_1</em> - Social Security Number,<br/>
                <em>f1_2_g_1</em> - state's drive license or I.D.,<br/>

                <em>f3_a_1</em> - additional requirements for state,<br/>
                <em>f2_a_1</em> - voter additional information,<br/>

                <em>envelope_address</em> - address of voter's region local official.
            </p>
            <p>
                Also object <strong><em>leo</em></strong> is available with following fields:<br/>

                /** Physical and Mailing Addresses */<br/>
                physical.addressTo, physical.street1, physical.street2, physical.city, physical.state, physical.zip, physical.zip4;<br/>
                mailing.addressTo, mailing.street1, mailing.street2, mailing.city, mailing.state, mailing.zip, mailing.zip4;<br/><br/>

                /** Local Election Official */
                leo.title, leo.firstName, leo.initial, leo.lastName, leo.suffix,<br/>
                leoPhone, leoFax, leoEmail, dsnPhone;<br/><br/>

                /** Local Overseas Election Official - new name  is Absentee Voter Clerk */<br/>
                lovc.title, lovc.firstName, lovc.initial, lovc.lastName, lovc.suffix,<br/>
                lovcPhone, lovcFax, lovcEmail;<br/> <br/>

                /* Additional contacts */ <br/>
                addContact.title, addContact.firstName, addContact.initial, addContact.lastName, addContact.suffix,<br/>
                addPhone, addEmail;<br/><br/>

                website, hours, furtherInstruction;<br/>
            </p>
        </div>
    </div>
    <div class="ft"></div>
</div>
<script type="text/javascript">
    var myCodeMirror = CodeMirror.fromTextArea( document.getElementById('flowFaceText'));
</script>