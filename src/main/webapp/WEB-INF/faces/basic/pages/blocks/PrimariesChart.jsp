<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="date" class="java.util.Date" />

<div class="row">
  <div class="col-xs-12 col-sm-12 <c:if test="${empty primaries}">text-center</c:if>">
    <div class="block-page-title-block">
      <h1 class="title">Primary Election Dates</h1>
    </div>
  </div>
  <div class="col-xs-12 col-sm-10 col-sm-offset-1">
    <table class="table table-responsive election-dates primary-election-dates">
      <thead>
        <th><h3>State</h3></th>
        <th><h3>Date of Primary</h3></th>
        <th><h3>Deadlines</h3></th>
        <th><h3>Voting Requirements</h3></th>
      </thead>
      <tbody>
      <c:forEach items="${states}" var="state" >
        <tr>
          <td><strong>${state.name}</strong></td>
          <td>
            <c:set var="election" value="${primaries[state.abbr]}"/>
            <c:choose>
              <c:when test="${not empty election}">${election.heldOn} - ${election.title}</c:when>
              <c:otherwise>New primary dates will be posted when available.</c:otherwise>
            </c:choose>
          </td>
          <td style="white-space: nowrap">
            <a href="<c:url value="/state-elections/state-election-dates-deadlines.htm"><c:param name="stateName">${state.abbr}</c:param></c:url>">Check Deadlines</a>
          </td>
          <td style="white-space: nowrap">
            <a href="<c:url value="/sviddomestic.htm"><c:param name="stateId">${state.id}</c:param><c:param name="submission" value="1"/></c:url>">View State Requirements</a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </div>
</div>


<script type="text/javascript">
  var table = document.querySelector("table.primary-election-dates");
  var thead = table.querySelector("table.primary-election-dates thead");

  var tableTop, tableBottom, theadHeight;
  var sticky = false;
  var theadGhost = thead.cloneNode(true);

  theadGhost.classList.add("sticky");
  theadGhost.classList.add("hidden");

  table.appendChild(theadGhost);

  var scrollTimeout = null;

  window.addEventListener("scroll", onScroll);

  function onScroll() {
    var scrollTop = window.scrollY;

    if (!scrollTimeout) {
      // scroll start, figure out table top position
      tableTop = table.offsetTop;
      theadHeight = thead.offsetHeight;
      var traverse = table.offsetParent;

      while (traverse) {
        tableTop += traverse.offsetTop;
        traverse = traverse.offsetParent;
      }

      tableBottom = tableTop + table.offsetHeight;
    }

    clearTimeout(scrollTimeout);
    scrollTimeout = setTimeout(function () {
      // scroll end
      scrollTimeout = clearTimeout(scrollTimeout);
    }, 1000);

    if (tableTop < scrollTop && scrollTop < tableBottom) {
      if (!sticky) {
        // show ghostThead
        sticky = true;
        theadGhost.classList.remove("hidden");
        freeze(theadGhost, thead);
      }
      if (scrollTop > tableBottom - theadHeight) {
        // move away when close to the bottom
        theadGhost.style.transform = `translate(0, ${
          tableBottom - scrollTop - theadHeight
        }px)`;
      } else {
        // reset move when not
        theadGhost.style.transform = "translate(0, 0)";
      }
    } else {
      if (sticky) {
        // hide ghostThead
        sticky = false;
        theadGhost.classList.add("hidden");
      }
    }
  }

  window.addEventListener("resize", function () {
    freeze(theadGhost, thead);
  });

  function freeze(el, from) {
    // calculate ghostThead widths recursively
    var traverse = el.firstChild;
    var traverseFrom = from.firstChild;

    if (el.style) {
      el.style.width = from.clientWidth + "px";
    }

    while (traverse && traverseFrom) {
      freeze(traverse, traverseFrom);
      traverse = traverse.nextSibling;
      traverseFrom = traverseFrom.nextSibling;
    }
  }
</script>
