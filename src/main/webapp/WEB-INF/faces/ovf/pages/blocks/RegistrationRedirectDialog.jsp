<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="modal fade registration-redirect-popup" id="registration-redirect-popup" tabindex="-1" role="dialog" aria-labelledby="registration-redirect-popupLabel"
     aria-hidden="true">
    <div class="modal-dialog registration-redirect-popup__container" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="registration-redirect-popup__content">
                    <div class="registration-redirect-popup__img"><img src="<c:url value="/img/registration-popup-people.jpg"/>" alt="People Registration US Vote" />
                    </div>
                    <div class="popup__box">
                        <div class="registration-redirect-popup__text">
                            <p><span class="registration-redirect-popup__title">Overseas Vote</span>is redirecting you to our parent site,
                                U.S. Vote Foundation, to generate your Overseas Voter Registration/Ballot Request form.</p>

                            <p>U.S. Vote Foundation offers complete voter services to all voter types including voters
                                abroad and uniformed services voters and their families.</p>
                        </div>
                        <button class="registration-redirect-popup__button" onclick="window.location.href='https://www.usvotefoundation.org/vote/voter-registration-absentee-voting.htm'">Lets go</button>
                        <div class="registration-redirect-popup__icon"><img src="<c:url value="/img/registration-popup-vote.png"/>" alt="US Vote"/></div>
                        <div class="registration-redirect-popup__text">
                            <p>You will be automatically redirected to the new website in 20 seconds...</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        // Registration Redirect
        let registrationRedirectInterval = null;
        $("#registration-redirect-popup").on("shown.bs.modal", function () {
            registrationRedirectInterval = setInterval(function(){
                window.location = "https://www.usvotefoundation.org/vote/voter-registration-absentee-voting.htm";
            },20000); // 20 second delay before automatic redirect
        });
        $("#registration-redirect-popup").on("hide.bs.modal", function () {
            clearInterval(registrationRedirectInterval);
        });
    });
</script>