<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>CTEP IAM SSO</title>
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
  </head>
  <body>
    <form method="POST" action="sendPostRequest.action">
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
      <h3><s:label name="message"/> </h3>
        <s:if test="hasActionMessages()">
        <div class="messages">
          <strong>Info:</strong>
          <br>
          <s:actionmessage/>
        </div>
      </s:if>
      <s:hidden name="idPSessionIndex"/>
    </form>
  </body>
</html>