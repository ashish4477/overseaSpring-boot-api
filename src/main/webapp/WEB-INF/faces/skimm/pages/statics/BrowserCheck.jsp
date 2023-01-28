<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<style>
/* Used to display browser incompatibility */
.message {
    display: none;
    position: fixed;
    left:0;
    width:100%;
    background-color:orange;
    z-index: 1;
    color:#fff;
    border-radius:0;
    z-index:2000;
}
.message p {text-align:center;}
</style>

<div id="browserWarning" class="alert alert-dismissible fade in message" role="alert">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
  <p><strong>This website works best with newer browsers like Edge, Safari, Chrome, and Firefox. Internet Explorer users may have issues downloading forms.</strong></p>
</div>

<script type="text/javascript" >
  function detectIE() {
    var ua = window.navigator.userAgent;

    var msie = ua.indexOf('MSIE ');
    if (msie > 0) {
        // IE 10 or older => return version number
        return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
    }

    var trident = ua.indexOf('Trident/');
    if (trident > 0) {
        // IE 11 => return version number
        var rv = ua.indexOf('rv:');
        return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
    }

    var edge = ua.indexOf('Edge/');
    if (edge > 0) {
       // Edge (IE 12+) => return version number
       return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
    }

    // other browser
    return false;
  }

  if(detectIE() && detectIE()<=11){
    $(document.body).css('margin-top', '54px');
   $('#browserWarning').show();    
  }
</script>