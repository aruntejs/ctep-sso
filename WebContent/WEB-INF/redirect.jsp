<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.net.*"%>
<%@ page import="com.westat.kjdk.app.KAppConfig"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>
<%
  String isLogin = (String)request.getSession().getAttribute("Select");
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
  defaultResponseLocation = defaultResponseLocation + "/ctsusso/spSingleSignOn.action";
  if(isLogin == null)
  {
    String urlWithSessionID = response.encodeRedirectURL(defaultResponseLocation);
    response.sendRedirect( urlWithSessionID );
  }
%>
<html>
  <head>
    <base target="_parent">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
    <title>CTEP IAM SSO</title>
  </head>
  <!--
  <body onload="document.getElementById('dummy').click();">
  -->
  <body onload="responseOnLoad();">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="hdrBG">
      <tr>
        <td align="center"><h2><font color="#ffffff">CTSU's SAML based Single Sign-On(SSO) federated authentication system using NCI CTEP-IAM</font></h2></td>
        <td align="right">
          <img src="graphics/ctsu_logo.gif" border="0" align="middle" alt="CTSU" title="Cancer Trials Support Unit">    
        </td>
      </tr> 
    </table>
    <table width="100%">
      <tr>
        <td align="right"><font size="1">SSO Version: <%=version%>. Release date: <%=versionDate%></font></td>
      </tr>
    </table>
    <s:a id="dummy" href="showResults.action">Loading ...</s:a>
    <script language="javascript">
      function responseOnLoad() {
        window.parent.location.href="showResults.action";
      }
    </script>  
  </body>
</html>