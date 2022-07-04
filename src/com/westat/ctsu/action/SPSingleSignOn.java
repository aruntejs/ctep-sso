package com.westat.ctsu.action;

import com.westat.ctsu.sso.ctep.CTEPSAMLRequest;
import com.westat.ctsu.sso.ctep.CTEPSAMLResponse;
import com.westat.ctsu.sso.ctep.CTEPSSOAuthenticator;
import com.westat.ctsu.sso.ctep.CTEPSSOUser;
import com.westat.ctsu.sso.framework.ZString;
import com.westat.ctsu.sso.framework.ZLogger;
import com.westat.ctsu.sso.framework.ZException;
import com.westat.ctsu.sso.framework.ZConstant;
import com.westat.ctsu.sso.domain.PersonRoster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import org.opensaml.ws.message.encoder.MessageEncodingException;

import com.opensymphony.xwork2.ActionSupport;

import com.westat.ctsu.sso.framework.ZConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SPSingleSignOn extends ActionSupport {
  private String message;
 

  private static Properties properties;

  static {
    try {
      properties = new Properties();
      properties.load(SPSingleSignOn.class.getClassLoader().getResourceAsStream("sp.properties"));
    } catch (Exception e) {
      System.out.println("Exception while loading sp.properties file " + e.getMessage());
    }
  }

  @Override
  public String execute() throws Exception {
    return isUserAuthenticated();
  }

  private String isUserAuthenticated() {
    String retString = "";
    try {
      if(isSAMLResponse(ServletActionContext.getRequest())) {
        retString = "validatedfromIdp";
      } else {
        ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
        retString = "showIdP";
      }
    } catch (Exception e) {
      ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
      retString = "showIdP";
    }
    return retString;
  }

  /**
   * To authenticate user. This method is invoked during Authentication Only.
   * 
   * @return
   */
  public String authenticateUser() {
    /**
      * Fetch all the custom properties.
      */
    String selected = (String)ServletActionContext.getRequest().getSession().getAttribute("Select");
    if(ZString.isBlank(selected)) {
      ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
      return "showIdP";
    }
    Map<String, String> attributeMap = processAttributeMap();
    
    /*
     * Deploy server name is necessary
     */
    if (ZString.isBlank(attributeMap.get("ResponseURL"))) {
      ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
      return "showIdP";
    }
    if (!StringUtils.equalsIgnoreCase(ServletActionContext.getRequest().getParameter("fromFlag"), 
                                      "iFrameUI") && 
        StringUtils.equalsIgnoreCase(attributeMap.get(ZConstant.loginMode), 
                                     ZConstant.alternate)) {
      return attributeMap.get(ZConstant.loginMode);
    } else {
      try {
        sendSAMLAuthNRequest(ServletActionContext.getResponse(), null, 
                             null, null, attributeMap, false);
      } catch (MessageEncodingException e) {
        ZLogger.log(this.getClass(), "SPSingleSignOn.authenticateUser exception " + e.getMessage());
        setError(e);
      }
    }
    return null;
  }

  /**
   * 
   */
  private Map<String, String> processAttributeMap() {
    Map<String, String> attributeMap = new HashMap<String, String>();
    Map<String, String> tempMap = 
      (HashMap<String, String>)ServletActionContext.getRequest().getSession().getAttribute("AttributeMap");
    if (StringUtils.equalsIgnoreCase(ServletActionContext.getRequest().getParameter("fromFlag"), 
                                     "iFrameUI")) {
      if (tempMap != null && tempMap.size() > 0) {
        attributeMap = tempMap;
      }
    } else {
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("roster")))
        attributeMap.put(ZConstant.rosterFlag, 
                         ServletActionContext.getRequest().getParameter("roster"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("appSource")))
        attributeMap.put(ZConstant.appSource, 
                         ServletActionContext.getRequest().getParameter("appSource"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("loginScreenMode")))
        attributeMap.put(ZConstant.loginMode, 
                         ServletActionContext.getRequest().getParameter("loginScreenMode"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("customStyle")))
        attributeMap.put(ZConstant.customCSS, 
                         ServletActionContext.getRequest().getParameter("customStyle"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("customGraphic")))
        attributeMap.put(ZConstant.customGraphic, 
                         ServletActionContext.getRequest().getParameter("customGraphic"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("responseURL")))
        attributeMap.put("ResponseURL", 
                         ServletActionContext.getRequest().getParameter("responseURL"));
      if (StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("idP")))
        attributeMap.put("idP", 
                         ServletActionContext.getRequest().getParameter("idP"));

      ServletActionContext.getRequest().getSession().setAttribute("AttributeMap", 
                                                                  attributeMap);
    }
    return attributeMap;
  }

  /**
   * IdP send response to this method after authenticating user identity.
   * 
   * @return
   */
  public String getAuthResponse() {
    HashMap attributeMap;
    try {
      if(!isSAMLResponse(ServletActionContext.getRequest())) {
        ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
        return "showIdP";
      }
      CTEPSSOAuthenticator sso = new CTEPSSOAuthenticator();
      CTEPSAMLResponse samlResponse = sso.getSAMLResponse(ServletActionContext.getRequest());
      processIdpRespnse(samlResponse);
      // In addition, let's get all the attributes from SAML response
      attributeMap = sso.getSAMLResponseAttributes(samlResponse.getResponse());
    } 
    catch (Exception e) {
      setMessage("'User Not Authenticated due to Exception'  --  message is " + 
                 e.getMessage());
      setError(e);
      return "error";
      
    }
    return "authenticatefromIdp";
  }

  @SuppressWarnings("rawtypes")
  private void processIdpRespnse(CTEPSAMLResponse samlResponse) throws Exception{
    Map<String, String> resultMap = new HashMap<String, String>();
    //
    // need to save the attribute map idpSessionId for pass through
    // 
    Map<String, String> attributeMap = (Map<String, String>)ServletActionContext.getRequest().getSession().getAttribute("AttributeMap");
    ServletActionContext.getRequest().getSession().setAttribute("Select", "Yes");
    try {
      CTEPSSOAuthenticator sso = new CTEPSSOAuthenticator();
      CTEPSSOUser user = sso.getSSOUser(samlResponse);

      if(attributeMap == null) {
        // this should not happens since the attributeMap is carried from request
        attributeMap = new HashMap<String, String>();
      }
      attributeMap.put("IdPSessionIndex", samlResponse.getIdpSessionIndex());
      String idpIssuer = samlResponse.getIdpIssuer();
      /**
       * need to know the CTEP IDP address based on the issuer name
       */
      attributeMap.put("idP", (String)properties.get(idpIssuer));
      
      /**
        * Retrive assertion attributes from the SAML response.
        */
      if(!ZString.isBlank(user.getEmailAddress()))
        resultMap.put("Subject/Name Id --> ", user.getEmailAddress());
      if(!ZString.isBlank(user.getPwdExpiryDays()))
        resultMap.put("IAM password expires in --> ", user.getPwdExpiryDays());
      if(!ZString.isBlank(user.getRegExpiryDays()))
        resultMap.put("IAM registration expires in --> ", user.getRegExpiryDays());
      if(!ZString.isBlank(user.getUserName()))
        resultMap.put("IAM user name --> ", user.getUserName());
      if(!ZString.isBlank(user.getFirstName()))
        resultMap.put("IAM user first name --> ", user.getFirstName());
      if(!ZString.isBlank(user.getLastName()))
        resultMap.put("IAM user last name --> ", user.getLastName());
      if(!ZString.isBlank(user.getPersonType()))
        resultMap.put("IAM user person type --> ", user.getPersonType());
      else
        resultMap.put("IAM user person type -->", "Not existed before CTEP IAM 2.0.11");
      resultMap.put("IAM CTEP Id -->", Integer.toString(user.getCtepId()));
      if(!ZString.isBlank(samlResponse.getIdpIssuer()))
        resultMap.put("Issuer name --> ", samlResponse.getIdpIssuer());
      if(samlResponse.getTokenExpireTime() != null) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        resultMap.put("Token expiration time --> ", dateFormat.format(samlResponse.getTokenExpireTime()));
      }
      
      if(!ZString.isBlank(user.getCtsuRoster()))
        resultMap.put("CTSU Roster --> ", user.getCtsuRoster());
      List<PersonRoster> rosterList = user.getPersonRosters();
      if (rosterList != null && rosterList.size() > 0) {
        for (PersonRoster roster : rosterList) {
          displayPersonRoster(resultMap, "Person group code --> ", roster.getGroupCode());
          displayPersonRoster(resultMap, "Person institution code --> ", roster.getInstitutionCode());
          displayPersonRoster(resultMap, "Person institution name --> ", roster.getInstitutionName());
          displayPersonRoster(resultMap, "Person roster status --> ", roster.getRosterStatus());
          displayPersonRoster(resultMap, "Person roster institution role --> ", roster.getInstitutionRole());
        }
      }
    }
    catch(Exception e) {
      // log to the file
      ZLogger.log(this.getClass(), "SPSingleSignOn.processIdpRespnse exception " + e.getMessage());
      setError(e);
    }
    ServletActionContext.getRequest().getSession().setAttribute("ResultMap", resultMap);
    ServletActionContext.getRequest().getSession().setAttribute("AttributeMap", attributeMap);
  }

  private void displayPersonRoster(Map<String, String> resultMap, 
    String heading, String value) {
    if(!ZString.isBlank(value))
      resultMap.put(heading, value);
  }

  public String processPassThroughOPEN() throws MessageEncodingException {
    // get the deployment environment
    // default to test for safety
    String openURL = (String)properties.get("OPENPRODUCTION");
    /*
    String openEnv = (String)ServletActionContext.getRequest().getSession().getAttribute("OPEN");
    if(!ZString.isBlank(openEnv)) {
      if(openEnv.equalsIgnoreCase("dev"))
        openURL = (String)properties.get("OPENDEV");
      else if(openEnv.equalsIgnoreCase("production"))
        openURL = (String)properties.get("OPENPRODUCTION");
    }
    */
    return processPassThrough(openURL, null);
  }
  
  protected String processPassThrough(String passThroughURL, String relay) throws MessageEncodingException {
    // the attribute map should contains the information need to perform pass 
    // through
    try {
      Map<String, String> attributeMap = (Map<String, String>)ServletActionContext.getRequest().getSession().getAttribute("AttributeMap");
      if(attributeMap != null) {
        String idPSessionIndex = attributeMap.get("IdPSessionIndex");
        String relayState = ServletActionContext.getRequest().getParameter("RelayState");
        if (StringUtils.isNotEmpty(relayState)) {
          ServletActionContext.getRequest().setAttribute("RelayState", relayState);
          attributeMap.put("RelayState", relayState);
        }
        if (StringUtils.isNotEmpty(relay)) {
          ServletActionContext.getRequest().setAttribute("RelayState", relayState);
          attributeMap.put("RelayState", relay);
          relayState = relay;
        }
        
        if(StringUtils.isNotEmpty(passThroughURL)) {
          attributeMap.put("ResponseURL", passThroughURL);
        }
        
        sendSAMLAuthNRequest(ServletActionContext.getResponse(), idPSessionIndex, 
                             passThroughURL, relayState, attributeMap, true);
      }
    } catch (Exception e) {
      ZLogger.log(this.getClass(), "SPSingleSignOn.processPassThrough exception " + e.getMessage());
      setError(e);
      return "error";
    }
    return null;
  }
  
  public String processPassThroughNET() throws MessageEncodingException {
    // get the deployment environment
    // default to test for safety
    String NETURL = (String)properties.get("NETSSOTEST");
    String NETEnv = (String)ServletActionContext.getRequest().getSession().getAttribute("NET");
    if(!ZString.isBlank(NETEnv)) {
      if(NETEnv.equalsIgnoreCase("dev"))
        NETURL = (String)properties.get("NETSSODEV");
      else if(NETEnv.equalsIgnoreCase("production"))
        NETURL = (String)properties.get("NETSSOPRODUCTION");
    }
    return processPassThrough(NETURL, null);
  }
  
  public String processPassThroughIMedidata() throws MessageEncodingException {
    String iMedidataURL = (String)properties.get("iMedidata");
    String relay = (String)properties.get("iMedidataRelay");
    return processPassThrough(iMedidataURL, relay);
  }
  
  public String processPassThroughInnovate() throws MessageEncodingException {
    String innovateURL = (String)properties.get("innovate");
    String relay = (String)properties.get("innovateRelay");
    return processPassThrough(innovateURL, relay);
  }
  
  public String processDeepLinkInnovate() throws MessageEncodingException {
    String innovateURL = (String)properties.get("innovate");
    String relay = null;
    String selectedSite = (String)ServletActionContext.getRequest().getParameter("site");
    String selectedSubject = (String)ServletActionContext.getRequest().getParameter("subject");
    String selectedCRF = (String)ServletActionContext.getRequest().getParameter("CRF");
    if(selectedSite != null && selectedSite.trim().length() > 0) 
    {
      //relay = "https://nci-ctep.mdsol.com/MedidataRave/SelectRole.aspx?page=SitePage.aspx&ID=" + selectedSite; (bad)
      //relay = "https://hdcvcl06-031.mdsol.com/MedidataRave/(S(fd3jsbrp1v0qbivrwcovc0vd))/HomePage.aspx?LD_StudySiteID=4375"; (good)
      relay = "https://nci-ctep.mdsol.com/MedidataRave/(S(fd3jsbrp1v0qbivrwcovc0vd))/HomePage.aspx?LD_StudySiteID=4375"; 
      //relay = "https://nci-ctep.mdsol.com/MedidataRave/HomePage.aspx?LD_StudySiteID=" + selectedSite; (bad)
      //relay = "https://nci-ctep.mdsol.com/MedidataRave/SelectRole.aspx?page=SitePage.aspx&LD_StudySiteID=" + selectedSite;
      relay = "https://nci-ctep.mdsol.com/MedidataRave/SelectRole.aspx?page=SitePage.aspx&ID=" + selectedSite; 
    }
    else if(selectedSubject != null && selectedSubject.trim().length() > 0)
    {
      relay = "https://nci-ctep.mdsol.com/MedidataRave/SelectRole.aspx?page=SubjectPage.aspx&ID=" + selectedSubject;
    }
    else if(selectedCRF != null && selectedCRF.trim().length() > 0)
    {
      relay = "https://nci-ctep.mdsol.com/MedidataRave/SelectRole.aspx?page=CRFPage.aspx&DP=" + selectedCRF;
    }
    relay = "https://hdcvcl06-031.mdsol.com/MedidataRAVE/SelectRole.aspx?page=SitePage.aspx&ID=15518";
    if(relay != null && relay.length() > 0)
      return processPassThrough(innovateURL, relay);
    else
      return " ";
  }
  
  public String processDeepLinkOpen() throws MessageEncodingException {
    HttpServletRequest request = ServletActionContext.getRequest();
    String protocol = request.getParameter("protocol");
    String step = request.getParameter("step");
    String pid = request.getParameter("pid");
    String trackNum = request.getParameter("trackNum");
    String relay = null;
    if(!ZString.isBlank(trackNum)) 
    {
      relay = "PatientSummary?TrackNum=" + trackNum;   
    }
    else
    {
      relay = "PatientSummary?ProtNum=" + protocol + "&Step=" + step + "&PID=" + pid;
    }
    // always use OPEN production
    String openURL = (String)properties.get("OPENPRODUCTION");
    return processPassThrough(openURL, relay);
  }

  private void sendSAMLAuthNRequest(HttpServletResponse response, 
                                    String subNameID, String responseLocation, 
                                    String relayState, 
                                    Map<String, String> attributeMap,
                                    boolean passThrough) throws MessageEncodingException {
    try {
      HttpServletRequest request = ServletActionContext.getRequest();
      String idP = attributeMap.get("idP");
      String destination = "";
      if (!ZString.isBlank(idP)) {
        destination = (String)properties.get(idP);
      } 
      if (ZString.isBlank(destination)) {
        destination = (String)properties.get("DefaultIdP.login.location");
        ServletActionContext.getServletContext().setAttribute("IdPAddress", destination);
      }
     
      CTEPSAMLRequest samlRequest = new CTEPSAMLRequest();
      if(ZString.isBlank(responseLocation)){
        responseLocation = "http://localhost:8080/ctsusso/spSingleSignOngetAuthResponse.action";
      }
  
      samlRequest.setHttpRequest(request);
      samlRequest.setHttpResponse(response);
      samlRequest.setResponseURL(responseLocation);
      
      // 
      // Below specify the IDP URL, please replace with your IDP URL
      //
      samlRequest.setIdpURL(destination);
      //
      // Below specify who initiate this request, please replace with your 
      // issuer value. The below should be registered with CTEP IAM to be 
      // included in the Federation.
      //
      samlRequest.setIssuer("http://test.sp.com");
      samlRequest.setIssuer("https://www66.imsweb.com/shibboleth-sp");
      samlRequest.setIssuer("http://dev.cmslogin.ecog.org");
      samlRequest.setIssuer("http://open.ctsu.org");
      // 
      // Below specify to sign this request with SP certificate
      //
      samlRequest.setSignRequest(false);
      
      //samlRequest.setIssuer("www.allianceforclinicaltrialsinoncology.org"); // mayo
      // 
      // Below custom attributes are saved from the UI 
      String rosterFlag = attributeMap.get(ZConstant.rosterFlag);
      String appSource = attributeMap.get(ZConstant.appSource);
      String customStyle = attributeMap.get(ZConstant.customCSS);
      String customGraphic = attributeMap.get(ZConstant.customGraphic);
      String loginMode = attributeMap.get(ZConstant.loginMode);
      String responseURL = attributeMap.get("ResponseURL");
      
      if(!ZString.isBlank(responseURL)) {
        samlRequest.setResponseURL(responseURL);
      }
      if(!ZString.isBlank(customStyle))
        samlRequest.setCustomCSSURL(customStyle);
      if(!ZString.isBlank(customGraphic))
        samlRequest.setCustomGraphicURL(customGraphic);
      if(!ZString.isBlank(rosterFlag) && rosterFlag.equalsIgnoreCase("yes")) {
        samlRequest.setNeedPersonRoster(true);
        if(!ZString.isBlank(appSource))
          samlRequest.setAppSource(appSource);
      }
      else {
        samlRequest.setNeedPersonRoster(false);
      }
      if(!ZString.isBlank(loginMode)) {
        if(loginMode.equalsIgnoreCase(ZConstant.alternate)) {
          samlRequest.setMiniLoginScreen(true);
        }
      }
      else {
        samlRequest.setMiniLoginScreen(false);
      }
      if(!ZString.isBlank(subNameID)) {
        samlRequest.setIdpSessionIndex(subNameID);
      }
      // set the deep link if prefer
      if(!ZString.isBlank(relayState)) {
        samlRequest.setRelayState(relayState);
      }
      CTEPSSOAuthenticator sso = new CTEPSSOAuthenticator();
      
      if(passThrough) {
        sso.sendPassThroughRequest(samlRequest); 
      }
      else {
        sso.sendLoginRequest(samlRequest);
      }
    }
    catch(ZException e) {
      ZLogger.log(this.getClass(), "SPSingleSignOn.sendSAMLAuthNRequest ZException. " + e.getMessage());
      setError(e);
    }
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  /*
   * Check if any SAML response in the request
   */
  private boolean isSAMLResponse(HttpServletRequest request) 
    throws Exception {
    boolean isResponse = false;
    try {
      // 
      // Check if the SAMLResponse parameter is in the post message, if it
      // is, then proceed to extract the SAML response
      //
      String samlResponseStr = request.getParameter("SAMLResponse");
      
      if(samlResponseStr != null) {
        // 
        // Create a CTEP authenticator provided by SSO framework. This object
        // will be used to invoke SSO API for SAML login request and for getting
        // SAML login response
        //
        CTEPSSOAuthenticator sso = new CTEPSSOAuthenticator();
        
        //
        // This is login request is samlResponse is null
        //
        CTEPSAMLResponse samlResponse = sso.getSAMLResponse(request);
        if (samlResponse != null) {
          processIdpRespnse(samlResponse);
          isResponse = true;
        }
      }
    }
    catch(Exception e) {
      // this is due to HttpServletRequest does not have the SAML response
      throw e;
    }
    return isResponse;
  }
  
  private void setError(Exception e) {
    Map<String, String> resultMap = new HashMap<String, String>();
    resultMap.put("Caught exception while processing SAML response. The exception message is ", e.getMessage());
    ServletActionContext.getRequest().getSession().setAttribute("ResultMap", resultMap);
  }
}
