<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
    <title>CTEP IAM SSO</title>
  </head>
  <body>
    <form>
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
      </table>
      <h4>
        <s:label name="message" />
        Error encountered
      </h4>
      <s:iterator value="#session.ResultMap" status="stat">
        <s:iterator>
          <s:property value="key"/>
          <s:property value="value"/> <br/>
        </s:iterator>
      </s:iterator>
    </form>
  </body>
</html>