<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.westat.kjdk.app.KAppConfig"%>
<%@ page import="java.net.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>
<%
  String ip = KAppConfig.getServer();
  String defaultResponseLocation;
  StringBuffer sb = request.getRequestURL();
  String url = sb.toString();
  URL serverUrl = new URL(url);
  String hostName = serverUrl.getHost();
  int hostPort = serverUrl.getPort();
  String protocol = serverUrl.getProtocol();
  if(hostName.indexOf("localhost") >= 0) {
    hostName = ip; 
  }
  defaultResponseLocation = protocol + "://" + hostName;
  if (hostPort > 0) {
    defaultResponseLocation = defaultResponseLocation + ":" + hostPort; 
  }
  defaultResponseLocation = defaultResponseLocation + "/ctsusso/spSingleSignOngetAuthResponse.action";
  request.getSession().setAttribute("Select", "Yes");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>CTEP IAM SSO test page</title>
    <meta http-equiv="Content-Language" content="en">
    <meta content="index, nofollow" name="robots"></meta>
    <meta name="description" content="CTEP Enterprise Applications listing">
    <meta name="keywords" content="CTEP, Enterprise Web, Secure Website, Timeline Reports,IPAD,OAOP, CTEP-IAM, IAM, Online Agent Order Processing, Integrated Platform for Agents and Diseases (IPAD)">
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
    <script src="script/script.js" type="text/javascript"></script>  
    <SCRIPT LANGUAGE="JavaScript">
      function submitToIDP (){
        document.forms[0].action='spSingleSignOnauthenticateUser.action';
        document.forms[0].submit();
      }
      function setAlternate(){
        var customStyle = document.forms[0].customStyle;;
        var customGraphic = document.forms[0].customGraphic;
        customGraphic.disabled = true;
        customGraphic.value = 'Not applicable';
        customStyle.disabled = false;
        if(customStyle.value.toLowerCase() == 'not applicable')
          customStyle.value = '';
      }
      function setNormal(){
        var customStyle = document.forms[0].customStyle;;
        var customGraphic = document.forms[0].customGraphic;
        customGraphic.disabled = false;
        if(customGraphic.value.toLowerCase() == 'not applicable')
          customGraphic.value = '';
        customStyle.disabled = true;
        customStyle.value = 'Not applicable';
      }
      function setRosterYes(){
        var appSource = document.forms[0].appSource;;
        appSource.disabled = false;
        if(appSource.value.toLowerCase() == 'not applicable')
          appSource.value = '';
      }
      function setRosterNo(){
        var appSource = document.forms[0].appSource;
        appSource.disabled = true;
        appSource.value = 'Not applicable';
      }
    </SCRIPT>
  </head>
  <body>
    <% request.getSession().setAttribute("Select", "Yes"); %>
    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="hdrBG">
            <tr>
              <td align="center"><h2><font color="#ffffff">CTSU's SAML based Single Sign-On(SSO) federated authentication system using NCI CTEP-IAM</font></h2></td>
              <td align="right">
                <img src="graphics/ctsu_logo.gif" border="0" align="middle" alt="CTSU" title="Cancer Trials Support Unit">    
              </td>
            </tr>        
          </table>
        </td>
      </tr> 
      <tr>
        <td align="right"><font size="1">SSO Version: <%=version%>. Release date: <%=versionDate%></font></td>
      </tr>
      <tr>
        <td height="100%" align="center" valign="top">
          <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="850">
            <tr>
              <td>
                <h3 align="center" > Interface to test CTSU SSO Starter Kit </h3>
              </td>
            </tr>
            <tr>
              <td valign="top" height="500">
                <table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" height="100%">
                  <tr>
                    <td width="100%" valign="top" style="padding:0in 0.5in 0in 0.5in;" align="left">
                      <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" class="contentBegins">					      
                        <tr>
                          <td colspan="2" width="100%">
                            <form method="POST" action="spSingleSignOnauthenticateUser.action">
                              <input type="hidden" name="SAMLRequest" value="request" /> 
                              <input type="hidden" name="RelayState" value="token" />
                              <table summary="" cellpadding="2" cellspacing="0" border="0" height="80%" width="100%">
                                <tr>
                                  <td colspan="2" width="100%">
                                    <font class="HeadingsmallBold">Custom Attributes:</font>
                                  </td>
                                </tr>					                
                                <tr>
                                  <td class="customdataTableHeader" width="25%">Attribute</td>
                                  <td class="customdataTableHeader" width="*">Input</td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <span style="color: red">*&nbsp;</span>
                                    <b title="Select 'No' will not request CTSU person roster information from CTEP IAM. Select 'Yes' will request CTSU person roster information from CTEP IAM.">Person Roster:</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="radio" value="No" name="roster" checked="checked" onClick="setRosterNo()"/> &nbsp;No<br/>
                                    <input type="radio" value="Yes" name="roster" onClick="setRosterYes()"/> &nbsp;Yes  
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <b title="Specify the application name. This value will drive if CTSU_ROSTER is customized response">Application Source:</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="text" id="appSource" name="appSource" disabled="disabled" size="75" value="Not applicable"/>				                       
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <span style="color: red">*&nbsp;</span>
                                    <b title="Select 'iFrame' mode will display CTEP IAM login content inside SPs login page iFrame. Select 'Page Redirect' mode will redirect to CTEP IAM URL.">Login Screen Mode:</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="radio" value="Alternate" name="loginScreenMode" checked="checked" onClick="setAlternate()"/> &nbsp;iFrame (Using IFRAME/Custom Style)<br/>
                                    <input type="radio" value="Normal" name="loginScreenMode" onClick="setNormal()"/> &nbsp;Page Redirect (Using redirect to IdP login URL)
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <b title="Specify the CSS URL for iFrame login mode. e.g. https://www.ctsu.org/open/docs/miniLogin.css">Custom Stylesheet(Public URL):</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="text" id="customStyle" name="customStyle" size="75"/>				                       
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <b title="Specify the jpg image URL to replace the CTEP IAM header for page redirect login mode.. e.g. https://www.ctsu.org/open/docs/ctsu.jpg">Custom Graphic(Public URL):</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="text" id="customGraphic" disabled="disabled" name="customGraphic" size="75" value="Not applicable"/>				                       
                                  </td>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <b title="Future implementation for deep link"><font color="Gray">Relay State:</font></b> 
                                  </td>
                                  <td class="formField">
                                    <input type="text" disabled="disabled" name="relayState" size="75"/>				                       
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <span style="color: red">*&nbsp;</span>
                                    <b title="Specify the SAML response URL where CTEP IAM will return the login response. e.g. http://10.10.10.10:8080/ctsusso/spSingleSignOngetAuthResponse.action ">SAML Response URL:</b> 
                                  </td>
                                  <td class="formField">
                                    <input type="text" name="responseURL" value="<%=defaultResponseLocation%>" size="75">
                                    </input>
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formLabel">&nbsp;
                                    <b title="Select 'IdPBETA' will invoke CTEP IAM beta SSO authenticator. Select 'IdPPRODUCTION' will invoke CTEP IAM production SSO authenticator.">IdP(For CTIS Use Only):</b> 
                                  </td>
                                  <td class="formField">
                                    <select id="idp" name="idP">
                                      <option value="CTEPPRODUCTION_2_0_10">CTEPPRODUCTION_2_0_10</option>
                                      <option value="CTEPPRODUCTION_2_0_11">CTEPPRODUCTION_2_0_11</option>
                                      <option value="CTEPBETA_2_0_10">CTEPBETA_2_0_10</option>
                                      <option value="CTEPBETA_2_0_11">CTEPBETA_2_0_11</option>
                                    </select>			                       
                                  </td>
                                </tr>
                                <tr>
                                  <td class="customFormField2" colspan="2" align="center" style="padding: 0.1in 0 0.1in 0">&nbsp;
                                    <input type="submit" value="Submit" class="customActionButton"/> 
                                  </td>
                                </tr>
                                <tr>
                                  <td colspan="2">
                                    <h5>The attribute marked with a <span style="color: red"> &nbsp;red star&nbsp;</span> is required field. If required attributes are left blank, you will not be able to submit the form.</h5>
                                  </td>
                              </table>
                            </form>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
        <!-- main content ends -->
            
          </table>      
        </td>
      </tr>
      <tr>
        <td width="100%">
          <!-- footer begins -->
          <table width="850" border="0" cellspacing="0" cellpadding="0" class="ftrTable" align="center">
            <tr>
              <td valign="top">
                <div align="center">
                  <a href="http://www.cancer.gov/" target="_blank"><img src="graphics/footer_nci.gif" width="63" height="31" alt="National Cancer Institute" border="0"></a>
                  <a href="http://www.dhhs.gov/" target="_blank"><img src="graphics/footer_hhs.gif" width="39" height="31" alt="Department of Health and Human Services" border="0"></a>
                  <a href="http://www.nih.gov/" target="_blank"><img src="graphics/footer_nih.gif" width="46" height="31" alt="National Institutes of Health" border="0"></a>
                  <a href="http://www.firstgov.gov/" target="_blank"><img src="graphics/footer_firstgov.gif" width="91" height="31" alt="FirstGov.gov" border="0"></a>
                </div>
              </td>
            </tr>
          </table>
      <!-- footer ends -->
        </td>
      </tr>
    </table>
  </body>
</html>