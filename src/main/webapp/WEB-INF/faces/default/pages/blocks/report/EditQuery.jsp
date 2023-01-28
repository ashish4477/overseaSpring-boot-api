<%--
  Created by IntelliJ IDEA.
  User: Leo
  Date: Jan 11, 2008
  Time: 7:22:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>


<script type="text/javascript" language="JavaScript">
    function disableAnswers( answerId, questionId, selectedIndx ) {
        var answerElem = document.getElementById( answerId );
        var questionElem = document.getElementById( questionId );
        if ( answerElem != null && questionElem != null ) {
            //alert(""+questionElem.selectedIndex +" ?? "+selectedIndx);
            if ( questionElem.selectedIndex == selectedIndx ) {
                answerElem.disabled = false;
            }
            else {
                answerElem.disabled = true;
            }
        }
    }
</script>

<!-- tree node prototype  -->
<link rel="stylesheet" type="text/css" href="http://developer.yahoo.com/yui/build/treeview/assets/skins/sam/treeview.css?_yuiversion=2.4.1" />
<style type="text/css">
    .ygtvcheck0 { background: url(http://developer.yahoo.com/yui/examples/treeview/assets/img/check/check0.gif) 0 0 no-repeat; width:16px; cursor:pointer }
    .ygtvcheck1 { background: url(http://developer.yahoo.com/yui/examples/treeview/assets/img/check/check1.gif) 0 0 no-repeat; width:16px; cursor:pointer }
    .ygtvcheck2 { background: url(http://developer.yahoo.com/yui/examples/treeview/assets/img/check/check2.gif) 0 0 no-repeat; width:16px; cursor:pointer }
</style>
<!--<script src = "http://yui.yahooapis.com/2.6.0/build/treeview/treeview-min.js" ></script>  -->
<script src="<c:url value="/js/treeview-min.js"/>" type="text/javascript"></script>
<!--<script src = "http://developer.yahoo.com/yui/examples/treeview/assets/js/TaskNode.js" ></script>-->
<script src="<c:url value="/js/TaskNode.js"/>" type="text/javascript"></script>

<script type="text/javascript">
(function() {
    var trees = [];
    var elementIds = [];

    var nodes = [];
    var nodeIndex;

    function treeInit() {
        YAHOO.log("Initializing TaskNode TreeView instance.")
        buildRandomTaskNodeTree();
    }



    YAHOO.util.Event.on("getchecked", "click", function(e) {
        YAHOO.log("Checked nodes: " + YAHOO.lang.dump(getCheckedNodes()), "info", "example");

        YAHOO.util.Event.preventDefault(e);
    });

    //Function  creates the tree and
    //builds between 3 and 7 children of the root node:
    function buildRandomTaskNodeTree() {

    <c:forEach items="${query.elements}" var="element" varStatus="groupId">
        elementIds[${groupId.index}] = ${element.id};
        trees[${groupId.index}] = new YAHOO.widget.TreeView("questionTree${groupId.index}");
        var columnNode${groupId.index} = new YAHOO.widget.TextNode("Select Questions", trees[${groupId.index}].getRoot(), false);

    <c:forEach items="${questionary}" var="page">
        var pageNode${groupId.index}_${page.id} = new YAHOO.widget.TaskNode(
        {label:"<em>Page</em> ${page.title}"},
                columnNode${groupId.index}, false);

    <c:forEach items="${page.questions}" var="group">
        var groupNode${groupId.index}_${group.id} = new YAHOO.widget.TaskNode(
        {label:"<em>Group</em> ${group.title}"},
                pageNode${groupId.index}_${page.id}, false);

    <c:forEach items="${group.variants}" var="variant">
        var variantNode${groupId.index}_${variant.id} = new YAHOO.widget.TaskNode(
        {label:"<em>Variant</em> ${variant.title}"},
                groupNode${groupId.index}_${group.id}, false);

    <c:forEach items="${variant.fields}" var="field">
    <c:set var="termSel" value="false"/>
    <c:set var="answerSelAll" value="false"/>
    <c:set var="theTermString" value=""/>
    <c:forEach items="${element.terms}" var="term">
    <c:if test="${field.id eq term.field.id}">
    <c:set var="termSel" value="true"/>
    <c:set var="theTermString" value="${term.answerRepresenting}"/>
    <c:if test="${fn:length(term.answers) eq 0}">
    <c:set var="answerSelAll" value="true"/>
    </c:if>
    </c:if>
    </c:forEach>

        var questionNode${groupId.index}_${field.id} = new YAHOO.widget.TaskNode(
        {label:"<em>Question</em> ${field.title}", id:${field.id}, type:"q"},
                variantNode${groupId.index}_${variant.id}, false, ${termSel});
    <c:choose>
    <c:when test="${field.type.templateName eq 'checkbox' or field.type.templateName eq 'checkbox_filled'}">
        var answerNode${groupId.index}_${field.id}_${option.id} = new YAHOO.widget.TaskNode(
        {label:"<em>Yes</em>", id:"true", type:"v"},
                questionNode${groupId.index}_${field.id}, false, ${answerSelAll or fn:contains(theTermString,"true")});
        var answerNode${groupId.index}_${field.id}_${option.id} = new YAHOO.widget.TaskNode(
        {label:"<em>No</em>", id:"false", type:"v"},
                questionNode${groupId.index}_${field.id}, false, ${answerSelAll or fn:contains(theTermString,"false")});

    </c:when>
    <c:otherwise>

    <c:forEach items="${field.options}" var="option">
    <c:set value="a${option.id}," var="ansIdNum"/>
        var answerNode${groupId.index}_${field.id}_${option.id} = new YAHOO.widget.TaskNode(
        {label:"<em>${option.viewValue}</em>", id:${option.id}, type:"a"},
                questionNode${groupId.index}_${field.id}, false, ${answerSelAll or fn:contains(theTermString,ansIdNum)});
    </c:forEach>
    <c:if test="${fn:length(field.options) eq 0}">
        var answerNode${groupId.index}_${field.id}_0 = new YAHOO.widget.TaskNode(
        {label:"<em>All answers</em>", id:0, type:"a"},
                questionNode${groupId.index}_${field.id}, false, ${answerSelAll});
    </c:if>

    </c:otherwise>
    </c:choose>
    </c:forEach>
    </c:forEach>
    </c:forEach>
    </c:forEach>

    </c:forEach>
        //		var answerNode1 = new YAHOO.widget.TaskNode("<em>Alaska</em>", questionNode, false);


        /*
               // Expand and collapse happen prior to the actual expand/collapse,
               // and can be used to cancel the operation
               tree.subscribe("expand", function(node) {
                      YAHOO.log(node.index + " was expanded", "info", "example");
                      // return false; // return false to cancel the expand
                   });

               tree.subscribe("collapse", function(node) {
                      YAHOO.log(node.index + " was collapsed", "info", "example");
                   });

               // Trees with TextNodes will fire an event for when the label is clicked:
               tree.subscribe("labelClick", function(node) {
                      YAHOO.log(node.index + " label was clicked", "info", "example");
                   });

               // Trees with TaskNodes will fire an event for when a check box is clicked
               tree.subscribe("checkClick", function(node) {
                      YAHOO.log(node.index + " check was clicked", "info", "example");
                   });

        */
        //The tree is not created in the DOM until this method is called:
        for( var i=0; i<trees.length; i++ ) {
            trees[i].draw();
        }
    }

    var callback = null;

    /*function onCheckClick(node) {
        YAHOO.log(node.label + " check was clicked, new state: " +
                node.checkState, "info", "example");
    }*/

    /*
        function checkAll() {
            var topNodes = trees[0].getRoot().children;
            for(var i=0; i<topNodes.length; ++i) {
                topNodes[i].check();
            }
        }

        function uncheckAll() {
            var topNodes = tree.getRoot().children;
            for(var i=0; i<topNodes.length; ++i) {
                topNodes[i].uncheck();
            }
        }
    */

    function onLabelClick(node) {
        new YAHOO.widget.TaskNode("new", node, false);
        node.refresh();
        return false;
    }


    // Gets the labels of all of the fully checked nodes
    // Could be updated to only return checked leaf nodes by evaluating
    // the children collection first.
    function getCheckedQueries(nodes) {
        checkedNodes = [];
        for(var i=0, l=nodes.length; i<l; i=i+1) {
            var n = nodes[i];
            if ( n.data.type == "q" ) {
                if (n.checkState > 0  ) { // if we were interested in the nodes that have some but not all children checked
                    checkedNodes.push(n);
                }
            }
            else if (n.hasChildren()) {
                checkedNodes = checkedNodes.concat(getCheckedQueries(n.children));
            }
        }
        return checkedNodes;
    }

    function treeOnSubmit() {
        var formElement = document.getElementById("EditQueryForm");
        if ( formElement != null ) {
            for ( var i=0; i<trees.length; i++) {
                var elemId = elementIds[i];
                var checkedTerms = getCheckedQueries(trees[i].getRoot().children);
                var termsStr = "0";
                for ( var j=0; j < checkedTerms.length; j++ ) {
                    var termNode = checkedTerms[j];
                    termsStr += "," + termNode.data.id;
                    if ( termNode.checkState != 2 ) {
                        var answerStr="0";
                        for ( var k=0; k < termNode.children.length; k++ ){
                            if ( termNode.children[k].checkState == 2 )
                                answerStr += ","+ termNode.children[k].data.id;
                        }
                        var newInput = document.createElement("input");
                        newInput.setAttribute("type","hidden");
                        newInput.setAttribute("name","answers_"+elemId + "_"+ termNode.data.id );
                        newInput.setAttribute("value",answerStr);
                        formElement.appendChild(newInput);
                    }
                }
                var newInput = document.createElement("input");
                newInput.setAttribute("type","hidden");
                newInput.setAttribute("name","fields_"+elemId);
                newInput.setAttribute("value",termsStr);
                formElement.appendChild(newInput);
            }
        }
        return true;
    }

    YAHOO.util.Event.onDOMReady(treeInit);
    YAHOO.util.Event.addListener("SaveButton", "click", function(){return treeOnSubmit();} );
})();
</script>

<div class="column-form">
    <div class="hd">
        <h2>Edit Query</h2>
    </div>
    <div class="bd">
        <div>
            <a href="<c:url value="/report/ReportQueryList.htm"/>">Back to Report List</a>
            &nbsp;&nbsp;
            <c:if test="${query.id ne 0}">
                &nbsp;&nbsp;
                <a href="<c:url value="/report/Results.htm"><c:param name="queryId" value="${query.id}"/></c:url>">View</a>
                &nbsp; &nbsp;
                <a href="<c:url value="/report/ResultsExport.htm"><c:param name="queryId" value="${query.id}"/></c:url> " target="_blank">Export</a>
            </c:if>
        </div>
        <form action="<c:url value="/report/EditQuery.htm"/>" method="post" id="EditQueryForm">
            <input type="hidden" value="${query.id}" name="queryId"/>
            <input type="hidden" name="submission" value="true"/>
            <table>
                <tr>
                    <th>Name</th>
                    <td>
                        <spring:bind path="query.name">
                            <input id="QueryName" type="text" name="${status.expression}" value="${status.value}"/>
                        </spring:bind>
                    </td>
                </tr>
                <%--<tr>
                    <th>Description</th>
                    <td>
                        <spring:bind path="query.description">
                            <textarea rows="5" cols="40" name="${status.expression}" >${status.value}</textarea>
                        </spring:bind>
                    </td>
                </tr>--%>
                <tr>
                    <th>Date range</th>
                    <td>
                        <spring:bind path="query.startRange">
                            <input type="text" name="${status.expression}" value="${status.value}"/>
                        </spring:bind>
                        -
                        <spring:bind path="query.endRange">
                            <input type="text" name="${status.expression}" value="${status.value}"/>
                        </spring:bind>
                        <br/>( format: mm/dd/yyyy )
                    </td>
                </tr>

                <authz:authorize ifAllGranted="ROLE_ADMIN">
                    <tr>
                        <th>&nbsp;</th>
                        <td>
                            <spring:bind path="query.applyFaces">
                                <script type="text/javascript">
                                    // god, this is such a hack. stupid spring. - nick
                                    YAHOO.util.Event.addListener("applyFacesCheckbox",'change', function() {
                                        document.getElementById("applyFaces").value = (this.checked) ? 1 : 0;  // 'true' : 'false';
                                        //document.getElementById("facesRow").style.display = (this.checked)? "inline" : "none";
                                    });
                                </script>
                                <input type="hidden" id="applyFaces" name="${status.expression}" value="${status.value}" />

                                <c:choose>
                                    <c:when test="${status.value}">
                                        <input type="checkbox" id="applyFacesCheckbox" checked="checked" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" id="applyFacesCheckbox" />
                                    </c:otherwise>
                                </c:choose>
                                <label>Add filter for states.</label>
                            </spring:bind>
                        </td>
                    </tr>
                    <tr id="facesRow">
                        <th>States</th>
                        <td>
                            <select name="fcList" multiple="multiple">
                                <c:forEach items="${faces}" var="face">
                                    <c:set var="selectorStr" value=""/>
                                    <c:if test="${fn:contains(query.faceRepresenting,face)}">
                                        <c:set var="selectorStr" value="selected"/>
                                    </c:if>
                                    <option value="${face}" ${selectorStr}>${face}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </authz:authorize>
                <authz:authorize ifNotGranted="ROLE_ADMIN">
                    <c:forEach items="${query.faces}" var="face">
                        <input type="hidden" name="fcList" value="${face}.faceName"/>
                    </c:forEach>

                </authz:authorize>
                <tr>
                    <th>Flow</th>
                    <td>
                        <spring:bind path="query.logType">
                            <select name="${status.expression}">
                                <option value="">Any</option>
                                <c:forEach items="${flows}" var="flow">
                                    <c:set var="selectorFlow" value=""/>
                                    <c:if test="${status.value eq flow}"><c:set var="selectorFlow" value="selected"/></c:if>
                                    <option value="${flow}" ${selectorFlow}>${flow}</option>
                                </c:forEach>
                            </select> 
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                </tr>
                <c:forEach items="${query.elements}" var="element"  varStatus="groupId">
                    <tr>
                        <th class="columnLabel">
                            <c:if test="${element.id eq 0}">
                                New column
                            </c:if>
                            <c:if test="${element.id ne 0}">
                                Column
                            </c:if>
                            <input type="text" name="number_${element.id}" value="${element.orderingNumber}" size="2">
                        </th>
                        <td class="columnQuestions">
                                ${fn:join(element.titles, ",")}
                            <div id="questionTree${groupId.index}"></div>
                        </td>
                    </tr>

                </c:forEach>
            </table>
            <table width="100%">
                <tr>
                    <td>
                        <input type="submit" value="Delete" name="delete"/>
                    </td>
                    <td class="rightAlign">
                        <input type="submit" value="Save" name="save" id="SaveButton"/>
                    </td>
                </tr>
             </table>
            <div>
                <a href="<c:url value="/report/ReportQueryList.htm"/>">Back to Report List</a>
                &nbsp;&nbsp;
                <c:if test="${query.id ne 0}">
                    &nbsp;&nbsp;
                    <a href="<c:url value="/report/Results.htm"><c:param name="queryId" value="${query.id}"/></c:url>">View</a>
                    &nbsp; &nbsp;
                    <a href="<c:url value="/report/ResultsExport.htm"><c:param name="queryId" value="${query.id}"/></c:url> " target="_blank">Export</a>
                </c:if>
            </div>
        </form>
    </div>
    <div class="ft"></div>
</div>
