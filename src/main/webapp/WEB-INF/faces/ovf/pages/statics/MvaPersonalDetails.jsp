<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<c:set var="showPasswordFields" value="true"/>
<c:if test="${not empty param.showPasswordFields}">
    <c:set var="showPasswordFields" value="${param.showPasswordFields}"/>
</c:if>

    <div class="row">
      <div class="col-xs-12 col-sm-6">
        <h3>Name</h3>
        <fieldset class="nameInfo">
         <div class="row form-group">
         <div class="Title col-xs-3">
          <form:label	for="name.title" path="name.title" cssErrorClass="error">Title</form:label>
               <form:select path="name.title" class="tiny form-control">
                <form:option value="" label=""/>
                <form:option value="Mr." label="Mr."/>
                <form:option value="Mrs." label="Mrs."/>
                <form:option value="Miss" label="Miss"/>
                <form:option value="Ms." label="Ms."/>
            </form:select>
            <form:errors path="name.title" class="errorMsg" />
        </div>
        <div class="firstName col-xs-6">
            <form:label	for="name.firstName" path="name.firstName" cssErrorClass="error" class="required">First *</form:label>
            <form:input id="firstName" path="name.firstName" class="medium form-control"/>
            <form:errors path="name.firstName" class="errorMsg" />
        </div>
        <div class="middleName col-xs-3">
            <form:label	for="name.middleName" path="name.middleName" cssErrorClass="error">Middle</form:label>
            <form:input id="middleName" path="name.middleName" class="small form-control"/>
            <form:errors path="name.middleName" class="errorMsg" />
        </div>
        </div>
        <div class="row form-group">
            <div class="lastName col-xs-9">
                <form:label	for="name.lastName" path="name.lastName" cssErrorClass="error" class="required">Last *</form:label>
                <form:input id="lastName" path="name.lastName" class="medium form-control" />
                <form:errors path="name.lastName" class="errorMsg" />
            </div>
            <div class="suffix col-xs-3">
                <form:label	for="name.suffix" path="name.suffix" cssErrorClass="error">Suffix</form:label>
                   <form:select path="name.suffix" class="small form-control">
                    <form:option value="" label=""/>
                    <form:option value="Jr" label="Jr"/>
                    <form:option value="Sr" label="Sr"/>
                    <form:option value="II" label="II"/>
                    <form:option value="III" label="III"/>
                    <form:option value="IV" label="IV"/>
                </form:select>
                <form:errors path="name.suffix" class="errorMsg" />
            </div>
          </div>
		</fieldset>
      </div>
      <div class="col-xs-12 col-sm-6">
        <h3>Contact Information</h3>
        <fieldset class="loginInfo">
            <div class="username form-group">
                <form:label	for="username" path="username" cssErrorClass="error" class="required control-label">Primary Email * <small>(used for login if applicable)</small>  </form:label>
                <form:input path="username" class="form-control username" id="username" />
                <form:errors path="username" class="errorMsg" />
                <spring:bind path="username">
                    <c:if test="${fn:contains(status.errorCode, 'username.exists')}">
                        <div id="forgot-password"><a href="<c:url value="/RemindPassword.htm"/>">Forgot Password?</a></div>
                    </c:if>
                </spring:bind>
            </div>

    <c:if test="${empty userDetails and userDetails.id ne 0}">
        <div class="usernameConfirmation form-group">
                <label for="usernameConfirmation" path="usernameConfirmation" cssErrorClass="error" class="usernameConfirmation required control-label"> Please Confirm Your Email *  <span id="confirmMessage"></span></label>
                <input path="usernameConfirmation" name="usernameConfirmation" id="usernameConfirmation" class="form-control usernameConfirmation" required/>
            </div>
    </c:if>
         
            <div class="password form-group">
                <c:if test="${showPasswordFields eq 'true'}">
                    <form:label	for="password" path="password" cssErrorClass="error" class="control-label">Password * <small>(At least six characters long)</small></form:label>
                    <form:input type="password" path="password" class="form-control" />
                    <form:errors path="password" class="errorMsg" />
                    <form:label	for="confirmPassword" path="confirmPassword" class="confirmPassword">Confirm Password *</form:label>
                    <form:input type="password" path="confirmPassword" class="form-control" />
                    <form:errors path="confirmPassword" class="errorMsg" />
                </c:if>
            </div>
        <div class="row phone form-group">
            <div class="col-xs-12 col-sm-8">
                <label class="control-label" for="CountryCallingCode">Country Code<span class="color-red strong">&nbsp;*</span></label>
                    <div class="controls" id="countryCodeSelect">
						<select class="form-control input-sm" id="countryCode" name="countryCode">
						<option value='0'>Select</option>
						<option value='93'>Afghanistan-93</option>
<option value='355'>Albania-355</option>
<option value='213'>Algeria-213</option>
<option value='1684'>American Samoa-1684</option>
<option value='376'>Andorra-376</option>
<option value='244'>Angola-244</option>
<option value='1264'>Anguilla-1264</option>
<option value='672'>Antarctica-672</option>
<option value='1268'>Antigua and Barbuda-1268</option>
<option value='54'>Argentina-54</option>
<option value='374'>Armenia-374</option>
<option value='297'>Aruba-297</option>
<option value='61'>Australia-61</option>
<option value='43'>Austria-43</option>
<option value='994'>Azerbaijan-994</option>
<option value='1242'>Bahamas-1242</option>
<option value='973'>Bahrain-973</option>
<option value='880'>Bangladesh-880</option>
<option value='1246'>Barbados-1246</option>
<option value='375'>Belarus-375</option>
<option value='32'>Belgium-32</option>
<option value='501'>Belize-501</option>
<option value='229'>Benin-229</option>
<option value='1441'>Bermuda-1441</option>
<option value='975'>Bhutan-975</option>
<option value='591'>Bolivia-591</option>
<option value='387'>Bosnia and Herzegovina-387</option>
<option value='267'>Botswana-267</option>
<option value='55'>Brazil-55</option>
<option value='246'>British Indian Ocean Territory-246</option>
<option value='1284'>British Virgin Islands-1284</option>
<option value='673'>Brunei-673</option>
<option value='359'>Bulgaria-359</option>
<option value='226'>Burkina Faso-226</option>
<option value='257'>Burundi-257</option>
<option value='855'>Cambodia-855</option>
<option value='237'>Cameroon-237</option>
<option value='1'>Canada-1</option>
<option value='238'>Cape Verde-238</option>
<option value='1345'>Cayman Islands-1345</option>
<option value='236'>Central African Republic-236</option>
<option value='235'>Chad-235</option>
<option value='56'>Chile-56</option>
<option value='86'>China-86</option>
<option value='57'>Colombia-57</option>
<option value='269'>Comoros-269</option>
<option value='682'>Cook Islands-682</option>
<option value='506'>Costa Rica-506</option>
<option value='385'>Croatia-385</option>
<option value='53'>Cuba-53</option>
<option value='599'>Curacao-599</option>
<option value='357'>Cyprus-357</option>
<option value='420'>Czech Republic-420</option>
<option value='243'>Democratic Republic of the Congo-243</option>
<option value='45'>Denmark-45</option>
<option value='253'>Djibouti-253</option>
<option value='1767'>Dominica-1767</option>
<option value='1809'>Dominican Republic-1809</option>
<option value='670'>East Timor-670</option>
<option value='593'>Ecuador-593</option>
<option value='20'>Egypt-20</option>
<option value='503'>El Salvador-503</option>
<option value='240'>Equatorial Guinea-240</option>
<option value='291'>Eritrea-291</option>
<option value='372'>Estonia-372</option>
<option value='251'>Ethiopia-251</option>
<option value='500'>Falkland Islands-500</option>
<option value='298'>Faroe Islands-298</option>
<option value='679'>Fiji-679</option>
<option value='358'>Finland-358</option>
<option value='33'>France-33</option>
<option value='689'>French Polynesia-689</option>
<option value='241'>Gabon-241</option>
<option value='220'>Gambia-220</option>
<option value='995'>Georgia-995</option>
<option value='49'>Germany-49</option>
<option value='233'>Ghana-233</option>
<option value='350'>Gibraltar-350</option>
<option value='30'>Greece-30</option>
<option value='299'>Greenland-299</option>
<option value='1473'>Grenada-1473</option>
<option value='1671'>Guam-1671</option>
<option value='502'>Guatemala-502</option>
<option value='441481'>Guernsey-441481</option>
<option value='224'>Guinea-224</option>
<option value='245'>GuineaBissau-245</option>
<option value='592'>Guyana-592</option>
<option value='509'>Haiti-509</option>
<option value='504'>Honduras-504</option>
<option value='852'>Hong Kong-852</option>
<option value='36'>Hungary-36</option>
<option value='354'>Iceland-354</option>
<option value='91'>India-91</option>
<option value='62'>Indonesia-62</option>
<option value='98'>Iran-98</option>
<option value='964'>Iraq-964</option>
<option value='353'>Ireland-353</option>
<option value='441624'>Isle of Man-441624</option>
<option value='972'>Israel-972</option>
<option value='39'>Italy-39</option>
<option value='225'>Ivory Coast-225</option>
<option value='1876'>Jamaica-1876</option>
<option value='81'>Japan-81</option>
<option value='441534'>Jersey-441534</option>
<option value='962'>Jordan-962</option>
<option value='7'>Kazakhstan-7</option>
<option value='254'>Kenya-254</option>
<option value='686'>Kiribati-686</option>
<option value='383'>Kosovo-383</option>
<option value='965'>Kuwait-965</option>
<option value='996'>Kyrgyzstan-996</option>
<option value='856'>Laos-856</option>
<option value='371'>Latvia-371</option>
<option value='961'>Lebanon-961</option>
<option value='266'>Lesotho-266</option>
<option value='231'>Liberia-231</option>
<option value='218'>Libya-218</option>
<option value='423'>Liechtenstein-423</option>
<option value='370'>Lithuania-370</option>
<option value='352'>Luxembourg-352</option>
<option value='853'>Macau-853</option>
<option value='389'>Macedonia-389</option>
<option value='261'>Madagascar-261</option>
<option value='265'>Malawi-265</option>
<option value='60'>Malaysia-60</option>
<option value='960'>Maldives-960</option>
<option value='223'>Mali-223</option>
<option value='356'>Malta-356</option>
<option value='692'>Marshall Islands-692</option>
<option value='222'>Mauritania-222</option>
<option value='230'>Mauritius-230</option>
<option value='262'>Mayotte-262</option>
<option value='52'>Mexico-52</option>
<option value='691'>Micronesia-691</option>
<option value='373'>Moldova-373</option>
<option value='377'>Monaco-377</option>
<option value='976'>Mongolia-976</option>
<option value='382'>Montenegro-382</option>
<option value='1664'>Montserrat-1664</option>
<option value='212'>Morocco-212</option>
<option value='258'>Mozambique-258</option>
<option value='95'>Myanmar-95</option>
<option value='264'>Namibia-264</option>
<option value='674'>Nauru-674</option>
<option value='977'>Nepal-977</option>
<option value='31'>Netherlands-31</option>
<option value='687'>New Caledonia-687</option>
<option value='64'>New Zealand-64</option>
<option value='505'>Nicaragua-505</option>
<option value='227'>Niger-227</option>
<option value='234'>Nigeria-234</option>
<option value='683'>Niue-683</option>
<option value='850'>North Korea-850</option>
<option value='1670'>Northern Mariana Islands-1670</option>
<option value='47'>Norway-47</option>
<option value='968'>Oman-968</option>
<option value='92'>Pakistan-92</option>
<option value='680'>Palau-680</option>
<option value='970'>Palestine-970</option>
<option value='507'>Panama-507</option>
<option value='675'>Papua New Guinea-675</option>
<option value='595'>Paraguay-595</option>
<option value='51'>Peru-51</option>
<option value='63'>Philippines-63</option>
<option value='64'>Pitcairn-64</option>
<option value='48'>Poland-48</option>
<option value='351'>Portugal-351</option>
<option value='1787'>Puerto Rico-1787</option>
<option value='974'>Qatar-974</option>
<option value='242'>Republic of the Congo-242</option>
<option value='262'>Reunion-262</option>
<option value='40'>Romania-40</option>
<option value='7'>Russia-7</option>
<option value='250'>Rwanda-250</option>
<option value='590'>Saint Barthelemy-590</option>
<option value='290'>Saint Helena-290</option>
<option value='1869'>Saint Kitts and Nevis-1869</option>
<option value='1758'>Saint Lucia-1758</option>
<option value='590'>Saint Martin-590</option>
<option value='508'>Saint Pierre and Miquelon-508</option>
<option value='1784'>Saint Vincent and the Grenadines-1784</option>
<option value='685'>Samoa-685</option>
<option value='378'>San Marino-378</option>
<option value='239'>Sao Tome and Principe-239</option>
<option value='966'>Saudi Arabia-966</option>
<option value='221'>Senegal-221</option>
<option value='381'>Serbia-381</option>
<option value='248'>Seychelles-248</option>
<option value='232'>Sierra Leone-232</option>
<option value='65'>Singapore-65</option>
<option value='1721'>Sint Maarten-1721</option>
<option value='421'>Slovakia-421</option>
<option value='386'>Slovenia-386</option>
<option value='677'>Solomon Islands-677</option>
<option value='252'>Somalia-252</option>
<option value='27'>South Africa-27</option>
<option value='82'>South Korea-82</option>
<option value='211'>South Sudan-211</option>
<option value='34'>Spain-34</option>
<option value='94'>Sri Lanka-94</option>
<option value='249'>Sudan-249</option>
<option value='597'>Suriname-597</option>
<option value='47'>Svalbard and Jan Mayen-47</option>
<option value='268'>Swaziland-268</option>
<option value='46'>Sweden-46</option>
<option value='41'>Switzerland-41</option>
<option value='963'>Syria-963</option>
<option value='886'>Taiwan-886</option>
<option value='992'>Tajikistan-992</option>
<option value='255'>Tanzania-255</option>
<option value='66'>Thailand-66</option>
<option value='228'>Togo-228</option>
<option value='690'>Tokelau-690</option>
<option value='676'>Tonga-676</option>
<option value='1868'>Trinidad and Tobago-1868</option>
<option value='216'>Tunisia-216</option>
<option value='90'>Turkey-90</option>
<option value='993'>Turkmenistan-993</option>
<option value='1649'>Turks and Caicos Islands-1649</option>
<option value='688'>Tuvalu-688</option>
<option value='1340'>U.S. Virgin Islands-1340</option>
<option value='256'>Uganda-256</option>
<option value='380'>Ukraine-380</option>
<option value='971'>United Arab Emirates-971</option>
<option value='44'>United Kingdom-44</option>
<option value='1'>United States-1</option>
<option value='598'>Uruguay-598</option>
<option value='998'>Uzbekistan-998</option>
<option value='678'>Vanuatu-678</option>
<option value='379'>Vatican-379</option>
<option value='58'>Venezuela-58</option>
<option value='84'>Vietnam-84</option>
<option value='681'>Wallis and Futuna-681</option>
<option value='212'>Western Sahara-212</option>
<option value='967'>Yemen-967</option>
<option value='260'>Zambia-260</option>
<option value='263'>Zimbabwe-263</option>
<option value='999999'>Other</option>						
                </select>
                </div>
            </div>
        
         <div class="col-xs-12 col-sm-8">
            <form:label for="phone" path="phone" cssErrorClass="error" class="control-label">Phone *</form:label>
            <form:input path="phone" type="tel" class="form-control" />
            <form:errors path="phone" class="errorMsg" />
        </div>
         <div class="col-xs-12 col-sm-4">
                <form:label for="phoneType" path="phoneType" cssErrorClass="error" class="control-label">Phone Type *</form:label>
                <form:select path="phoneType" class="form-control" >
                <form:option value="" label=""/>
                <form:option value="Home" label="Home"/>
                <form:option value="Work" label="Work"/>
                <form:option value="Mobile" label="Mobile"/>
                <form:option value="Other" label="Other"/>
            </form:select>
                <form:errors path="phoneType" class="errorMsg" />
            </div>
        </div>
        </fieldset>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-12 col-sm-6">
      <div class="accordion prev">
		  <div class="legend"> <!-- needed for accordion -->
       <h3><span>Previous Name</span></h3>
       <small>(Use this option if you want to update your name)</small>
    </div>
		<fieldset class="previousName nameInfo">
      <div class="row form-group">
         <div class="Title Title col-xs-2 col-sm-3">
          <form:label	for="previousName.title" path="previousName.title" cssErrorClass="error" class="control-label">Title</form:label>
               <form:select path="previousName.title" class="tiny form-control">
                <form:option value="" label=""/>
                <form:option value="Mr." label="Mr."/>
                <form:option value="Mrs." label="Mrs."/>
                <form:option value="Miss" label="Miss"/>
                <form:option value="Ms." label="Ms."/>
            </form:select>
            <form:errors path="previousName.title" class="errorMsg" />
        </div>
        <div class="firstName col-xs-6">
            <form:label	for="previousName.firstName" path="previousName.firstName" cssErrorClass="error" class="required control-label">First</form:label>
            <form:input path="previousName.firstName" class="medium form-control"/>
            <form:errors path="previousName.firstName" class="errorMsg" />
        </div>
        <div class="middleName col-xs-4 col-sm-3">
            <form:label	for="previousName.middleName" path="previousName.middleName" cssErrorClass="error" class="control-label">Middle</form:label>
            <form:input path="previousName.middleName" class="small form-control"/>
            <form:errors path="previousName.middleName" class="errorMsg" />
        </div>
      </div>
      <div class="row form-group">
        <div class="lastName col-xs-10 col-sm-9">
            <form:label	for="previousName.lastName" path="previousName.lastName" cssErrorClass="error" class="required control-label">Last</form:label>
            <form:input path="previousName.lastName" class="medium form-control" />
            <form:errors path="previousName.lastName" class="errorMsg" />
        </div>
        <div class="suffix col-xs-2 col-sm-3">
            <form:label	for="previousName.suffix" path="previousName.suffix" cssErrorClass="error" class="control-label">Suffix</form:label>
               <form:select path="previousName.suffix" class="small form-control">
                <form:option value="" label=""/>
                <form:option value="Jr" label="Jr"/>
                <form:option value="Sr" label="Sr"/>
                <form:option value="II" label="II"/>
                <form:option value="III" label="III"/>
                <form:option value="IV" label="IV"/>
            </form:select>
            <form:errors path="previousName.suffix" class="errorMsg" />
        </div>
      </div>
		</fieldset>
      </div> </div>
      <div class="col-xs-12 col-sm-6">
        <div class="<c:if test="${wizardContext.flowType ne 'FWAB'}">accordion alt</c:if>">
        <div class="legend"> <!-- needed for accordion -->
          <h3><span>Alternate Contact Information</span></h3>
          <small>&nbsp;</small>
        </div>
        <fieldset class="alternateContact">
        <div class="alternateEmail form-group">
                <form:label	for="alternateEmail" path="alternateEmail" cssErrorClass="error" class="control-label">Alternate Email</form:label>
                <form:input path="alternateEmail" type="email" class="form-control"/>
                <form:errors path="alternateEmail" class="errorMsg" />
            </div>
            <div class="alternatePhone form-group">
              <div class="col-xs-12 col-sm-8" style="padding-left: 0;">
                <form:label	for="alternatePhone" path="alternatePhone" cssErrorClass="error" class="control-label">Alternate Phone<small> (work phone, cell phone)</small></form:label>
                <form:input path="alternatePhone" type="tel" class="form-control"/>
                <form:errors path="alternatePhone" class="errorMsg" />
              </div>
              <div class="col-xs-12 col-sm-4">
                <form:label for="alternatePhoneType" path="alternatePhoneType" cssErrorClass="error" class="control-label">Phone Type</form:label>
                <form:select path="alternatePhoneType" class="form-control" >
                  <form:option value="" label=""/>
                  <form:option value="Home" label="Home"/>
                  <form:option value="Work" label="Work"/>
                  <form:option value="Mobile" label="Mobile"/>
                  <form:option value="Other" label="Other"/>
                </form:select>
                <form:errors path="alternatePhoneType" class="errorMsg" />
              </div>
            </div>
		</fieldset>
		</div>
      </div>
   </div>
     <div class="row form-group">
      <div class="col-xs-12 col-sm-6">
        <h3>Birth Date</h3>
        <fieldset class="birthInfo">
          <div class="row">
            <div class="birthMonth form-group col-xs-4">
                <form:label	for="birthMonth" path="birthMonth" cssErrorClass="error" class="required control-label">Month *</form:label>
                <form:select  id="birthMonth" path="birthMonth" cssErrorClass="error" class="form-control">
                    <form:option value="0" label="Select"/>
                    <c:forEach var="i" begin="1" end="12" step="1" varStatus ="status">
                        <form:option value="${i}" label="${i}"/>
                    </c:forEach>
                </form:select>
            </div>
            <div class="birthDate form-group col-xs-4">
                <form:label	for="birthDate" path="birthDate" cssErrorClass="error" class="required control-label">Day *</form:label>
                <form:select id="birthDate" path="birthDate" cssErrorClass="error" class="form-control">
                    <form:option value="0" label="Select"/>
                    <c:forEach var="i" begin="1" end="31" step="1" varStatus ="status">
                        <form:option value="${i}" label="${i}"/>
                    </c:forEach>
                </form:select>
            </div>
            <div class="birthYear form-group col-xs-4">
                <form:label	for="birthYear" path="birthYear" cssErrorClass="error" class="required control-label">Year *</form:label>
                <form:select id="birthYear" path="birthYear" cssErrorClass="error" class="form-control">
                <c:set var="yearNumb" value="${yearNumber-16}" scope="page"></c:set>
                    <form:option value="0" label="Select"/>
                    <c:forEach begin="1900" end="${yearNumb}" var="i" step="1">
                      <c:set var="j" value="${1900+yearNumb-i}"></c:set>
                        <form:option value="${j}" label="${j}"/>
                    </c:forEach>
                </form:select>
            </div>
            <form:errors path="birthMonth" class="errorMsg" />
            <form:errors path="birthDate" class="errorMsg" />
            <form:errors path="birthYear" class="errorMsg" />
        </fieldset>
      </div>
      <div class="col-xs-12 col-sm-6">
        <h3>Personal Details</h3>
        <fieldset class="personalInfo">
            	<div class="gender form-group">
                	<form:label	for="gender" path="gender" cssErrorClass="error" class="label-control">Sex <c:if test="${empty userValidationFieldsToSkip['gender']}">*</c:if></form:label>
                	<form:select id="gender" path="gender" cssErrorClass="error" class="form-control">
                        <form:option value="" label="Select"/>
                        <form:option value="M" label="Male"/>
                        <form:option value="F" label="Female"/>
                    </form:select>
                    <form:errors path="gender" class="errorMsg" />
                </div>
        </fieldset>
      </div>
    </div>