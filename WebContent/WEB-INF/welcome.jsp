<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.net.*"%>
<%@ page import="com.westat.kjdk.app.KAppConfig"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>
<%
  String isLogin = (String)request.getSession().getAttribute("Select");
  String ip = KAppConfig.getServer();
  String defaultResponseLocation;
  StringBuffer sb = request.getRequestURL();
  String url = sb.toString().toLowerCase();
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
  defaultResponseLocation = defaultResponseLocation + "/ctsusso/spSingleSignOn.action";
  if(isLogin == null)
  {
    String urlWithSessionID = response.encodeRedirectURL(defaultResponseLocation);
    response.sendRedirect( urlWithSessionID );
  }
  else
  {
    request.getSession().removeAttribute("Select");
  }
  // coming from wbdcr01, then go to wbdcr01
  System.out.println("url " + url);
  if(url.indexOf("wbdcr01") >= 0 || url.indexOf("ctsuas09") >= 0) {
    request.getSession().setAttribute("OPEN", "Dev"); 
    request.getSession().setAttribute("NET", "Dev");
  }
  else if(url.indexOf("test") >= 0) {
    request.getSession().setAttribute("OPEN", "Test");
    request.getSession().setAttribute("NET", "Test");
  }
  else {
    // for now, let's only use test since this may not be ready for testing yet
    //request.getSession().setAttribute("Open", "Production");
    //request.getSession().setAttribute("NET", "Production");
    request.getSession().setAttribute("OPEN", "Test");
    request.getSession().setAttribute("NET", "Test");
  }
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
    <title>CTEP IAM SSO</title>
    <script type="text/javascript">
      function submitOPENProd (){
        var action = "processPassThroughRequestOPEN.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
      function submitNET (){
        var action = "processPassThroughRequestNET.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
      function submitIMedidata (){
        var action = "processPassThroughRequestIMedidata.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
      function submitInnovate (){
        var action = "processPassThroughRequestInnovate.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
      function submitDeepLinkInnovate (){
        var action = "processDeepLinkRequestInnovate.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
      function submitDeepLinkOpen (){
        var action = "processDeepLinkRequestOpen.action";
        document.forms[0].action=action;
        document.forms[0].submit();
      }
    </script>
  </head>
  <body>
    <form>
      <% request.getSession().removeAttribute("Select"); %>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="hdrBG">
        <tr>
          <td align="center"><h2><font color="#ffffff">CTSU's SAML based Single Sign-On(SSO) federated authentication system using NCI CTEP-IAM</font></h2></td>
          <td align="right">
            <img src="graphics/ctsu_logo.gif" border="0" align="middle" alt="CTSU" title="Cancer Trials Support Unit">    
          </td>
        </tr> 
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="right"><font size="1">SSO Version: <%=version%>. Release date: <%=versionDate%></font></td>
        </tr>
        <tr>
          <td width="100%">
            <h3 align="center"> This is the first page of the organization after successful login</h3>
          </td>
        </tr>
      </table>
      <div style="border:2px solid black;">
      <table>
        <tr>
          <td colspan="4"> <h4>Pass through Authentication Demonstration</h4></td>
        </tr>
        <tr>
          <td>
            <s:a title="Link to OPEN home page without login" href="#" onclick="submitOPENProd();">
              <img src="graphics/open_prod.jpg" alt="Pass through link to OPEN"
            </s:a>
          </td>
          <td>
            <s:a title="Link to .NET SSO welcome page without login" href="#" onclick="submitNET();">
              <img src="graphics/ctsu_sso_net.jpg" alt="Pass through link to .NET SSO"
            </s:a>
          </td>
          <td>
            <s:a title="Link to iMedidata production home page without login" href="#" onclick="submitIMedidata();">
              <img src="graphics/iMedidata_prod.jpg" alt="Pass through link to iMedidata production"
            </s:a>
          </td>
          <td>
            <s:a title="Link to iMedidata innovate home page without login" href="#" onclick="submitInnovate();">
              <img src="graphics/iMedidata_innovate.jpg" alt="Pass through link to iMedidata innovate"
            </s:a>
          </td>
        </tr>
      </table>
      </div>
      <div style="border:2px solid black;">
      <table>
        <tr>
          <td colspan="2"> <h4>Rave Deep Link Authentication Demonstration (Study 9177, NCI-CTEP2 CTSU Rave)</h4> </td>
        </tr>
        <tr>
          <td>
            <s:a title="Deep link to Rave site or subject page" href="#" onclick="submitDeepLinkInnovate();">
              <img src="graphics/Rave_deep_link.jpg" alt="Deep link to Rave site or subject page"
            </s:a>
          </td>
          <td>
            <table>
              <tr>
                <td class="formLabel">&nbsp;
                  <b title="Specify site. Example: MN008">Site:</b> 
                </td>
                <td class="formLabel">&nbsp;
                  <b title="Specify subject.">Subject:</b> 
                </td>
                <td class="formLabel">&nbsp;
                  <b title="Specify CRF. Example: Enrollment form">CRF:</b> 
                </td>
              </tr>
              <tr>
                <td>
                  <select name="site">
                    <option value=" ">...</option>
                    <option value="4375">Abbot-Northwestern Hospital</option>
                    <option value="8532">Abramson Cancer Center of The University</option>
                    <option value="8531">CTSUTST01</option>
                  </select>
                </td>
                <td>
                  <select name="subject">
                    <option value=" ">...</option>
                    <option value="2110">625-790</option>
                    <option value="2969">Marchtue</option>
                    <option value="2168">878hh</option>
                  </select>
                </td>
                <td>
                  <select name="CRF">
                    <option value=" ">...</option>
                    <option value="12189">878hh Demo</option>
                    <option value="17730">13095 Step</option>
                  </select>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      </div>
      <div style="border:2px solid black;">
      <table>
        <tr>
          <td colspan="4"> <h4>OPEN Deep Link Authentication Demonstration</h4> </td>
        </tr>
        <tr>
          <td>
            <s:a title="Deep link to OPEN production patient summary" href="#" onclick="submitDeepLinkOpen();">
              <img src="graphics/open_deep_link.jpg" alt="Deep link to OPEN production patient summary"
            </s:a>
          </td>
          <td colspan="2">
            <table>
              <tr>
                <td class="formLabel">&nbsp;
                  <b title="Specify protocol. Example: E2511">CTEP Protocol:</b> 
                </td>
                <td class="formField">
                  <input type="text" id="protocol" name="protocol" size="75"/>				                       
                </td>
              </tr>
              <tr>
                <td class="formLabel">&nbsp;
                 <b title="Specify protocol step. Example: 1, 2, 3..">CTEP Protocol Step:</b> 
                </td>
                <td class="formField">
                 <input type="text" id="step" name="step" size="75"/>				                       
               </td>
              </tr>
              <tr>
                <td class="formLabel">&nbsp;
                 <b title="Patient ID">Patient ID:</b> 
                </td>
                <td class="formField">
                 <input type="text" id="pid" name="pid" size="75"/>				                       
               </td>
              </tr>
            </table>
          </td>
          <td>
            <table>
              <tr>
                <td class="formLabel">&nbsp;
                  <b title="Specify OPEN tracking number. The input should be numeric">OPEN tracking number:</b> 
                </td>
                <td class="formField">
                  <input type="text" id="trackNum" name="trackNum" size="75"/>				                       
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table> 
      </div>
      <h4>
        <s:label name="message" />
        Below shows user account information returned from CTEP IAM
      </h4>
      <s:iterator value="#session.ResultMap" status="stat">
        <s:iterator>
          <s:property value="key"/>
          <s:property value="value"/> <br/>
        </s:iterator>
      </s:iterator>
      <input type="hidden" name="SAMLResponse" value="response" />
      <s:hidden name="idPSessionIndex"/>
    </form>
  </body>
</html>