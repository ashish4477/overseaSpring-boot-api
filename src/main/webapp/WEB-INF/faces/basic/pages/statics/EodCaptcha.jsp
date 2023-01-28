<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:if test="${showCaptcha}">
  <script src="https://www.google.com/recaptcha/api.js" async defer></script>
  <div class="g-recaptcha" data-sitekey="${greSiteKey}"></div>
  <br/>
</c:if>
