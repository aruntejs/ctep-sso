<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="version.jsp" %>

<html>
  <head>
    <title>CTEP IAM SSO</title>
    <link rel="stylesheet" type="text/css" href="style/styleSheet.css" />
    <link rel="stylesheet" type="text/css" href="style/CustomStyle.css"/>
  </head>
  <body topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0">
    <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0" align="center">    
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
        <td>
          <h3 align="center"> This is the existing Login page of the organization </h3>
        </td>
      </tr>
      <tr>
        <td align="center">
          <table width="955px" height="800px" cellpadding="0" cellspacing="0" border="0" >
            <tr>
              <td>
              </td>
              <td>
                <table width="100%" height="690px" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td width="60%" align="center"> <h3> Welcome to the application </h3>
                    	Lorem ipsum dolor sit amet, scripserit consectetuer his at, est cu labore
                        complectitur, vis et iudico impedit similique. Nam mollis perfecto indoctum id, ne sed
                        omnesque antiopam. Cu zril oratio deserunt nec, an has autem invidunt oportere.
                        Omnesque similique at sit, nam eu dicant integre sententiae, pri dicant iracundia
                        at. Probo appareat rationibus an vel, nisl diam dolore ex sit. Eum in magna
                        laoreet, sit reque commodo laboramus ad. Ut est nisl omittam. Ea pro euripidis
                        liberavisse, mel cu aperiri pericula, ei postea epicurei evertitur qui. Duis mollis
                        invidunt duo id, ea mel mundi incorrupte. Duo elit novum iisque ad, an sonet indoctum
                        petentium eum. Summo congue eu vis, ei mel labores civibus adversarium.
                    </td>
                    <td height="30%">
                      <table height="100%" width="100%" cellpadding="0" border="0">
                        <tr valign="top">
                          <td height="100%">
                            <iframe id="loginFrame" width="100%" style="width: 400px;height: 430px" frameborder="0"
                              src="<%=request.getContextPath()%>/spSingleSignOnauthenticateUser.action?fromFlag=iFrameUI">
                            </iframe>  
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr valign="top">
                    <td height="60%">                          
                      <table class="mainTbl" width="100%" height="100%" cellpadding="3" >
                        <tr>
                          <td class="mainBoxHdg">
                          </td>
                        </tr>
                        <tr valign="top">
                          <td height="100%">
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td>
                      <table width="100%" height="100%" cellpadding="1" cellspacing="1" border="0">
                        <tr>
                          <td height="50%">
                            <table class="sideTbl" width="100%" height="100%" cellpadding="0" border="0">
                              <tr>
                                <td class="sideBoxHdg"><label></label>
                                </td>
                              </tr>
                              <tr valign="top">
                                <td height="100%">
                                  <table width="100%" cellspacing="3" cellpadding="3" border="0">
                                    <tr valign="top">
                                      <td><label></label>
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </table>        
                          </td>
                        </tr>
                        <tr>
                          <td height="50%">
                            <table class="sideTbl" width="100%" height="100%" cellpadding="0" border="0" >
                              <tr>
                                <td class="sideBoxHdg"><label></label>
                                </td>
                              </tr>
                              <tr valign="top">
                                <td height="100%">
                                  <table width="100%" cellspacing="3" cellpadding="3" border="0">
                                    <tr valign="top">
                                      <td><label></label>
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
                <table width="100%" height="10px" cellpadding="1" cellspacing="1" border="0">
                  <tr>
                    <td class="footer" align="center">&nbsp;
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </body>
</html>