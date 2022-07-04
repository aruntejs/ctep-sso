package com.westat.ctsu.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.common.binding.encoding.SAMLMessageEncoder;
import org.opensaml.saml2.binding.decoding.HTTPPostSimpleSignDecoder;
import org.opensaml.saml2.binding.encoding.HTTPPostSimpleSignEncoder;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.SessionIndex;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.LogoutRequestBuilder;
import org.opensaml.saml2.core.impl.LogoutResponseBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.core.impl.StatusMessageBuilder;
import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.BasicX509Credential;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Mock SP Log out.
 * 
 * @author Dkumar
 * 
 */
public class SPSingleLogOut extends ActionSupport {

    private static final long serialVersionUID = 3258788405312253108L;
    private String idpAddress;

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(SPSingleSignOn.class.getClassLoader().getResourceAsStream("sp.properties"));
        } catch (Exception e) {
            System.out.println("Exception while loading CES Properties file " + e.getMessage());
        }
    }
    static String issuerURL = (String) properties.get("issuerUrl");
    static String responseLocation = (String) properties.get("logout.responseLocation");
    private static String statusCode;

    @Override
    public String execute() throws Exception {
        String nameId = (String) ServletActionContext.getServletContext().getAttribute("IdPSessionIndex");
        System.out.println("SP1 exceute Method" + nameId);
        String statusMessage = responseRequestSwitch(nameId);
        if (statusMessage != null) {
            return "Logout";
        } else {
            return null;
        }
    }

    /**
     * This method acts as a switch based on the incoming HTTP Servlet Request.
     * 
     * @param nameId
     * @throws Exception
     */
    private String responseRequestSwitch(String nameId) throws Exception {
        try {
            SAMLMessageContext messageContext;
            messageContext = extractSAMLMessageContext(ServletActionContext.getRequest());

            SAMLObject message = messageContext.getInboundSAMLMessage();
            if (message instanceof LogoutResponse) {
                System.out.println("message is instance of log out response hence calling getLogoutResponse");
                getLogoutResponse();
                return statusCode;
            } else if (message instanceof LogoutRequest) {
                System.out.println("message is an instance of log out request hence calling sendLogoutResponse");
                sendLogoutResponseToIDP(getStatus(), ((LogoutRequest) message).getSessionIndexes().get(0)
                        .getSessionIndex(), ServletActionContext.getResponse());
                return null;
            } else {
                sendLogoutRequest(ServletActionContext.getResponse(), nameId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("exception in switch hence calling sendLogoutRequest");
            sendLogoutRequest(ServletActionContext.getResponse(), nameId);
            return null;
        }

    }

    /**
     * generates and returns a Status
     * 
     * @return
     */
    private Status getStatus() {
        StatusBuilder statusBuilder = new StatusBuilder();
        Status status = statusBuilder.buildObject(Status.DEFAULT_ELEMENT_NAME);
        StatusCodeBuilder statusCodeBuilder = new StatusCodeBuilder();
        StatusCode statusCode = statusCodeBuilder.buildObject(StatusCode.DEFAULT_ELEMENT_NAME);
        statusCode.setValue(StatusCode.SUCCESS_URI);
        status.setStatusCode(statusCode);
        StatusMessageBuilder statusMessageBuilder = new StatusMessageBuilder();
        StatusMessage statusMessage = statusMessageBuilder.buildObject(StatusMessage.DEFAULT_ELEMENT_NAME);
        statusMessage.setMessage("Success");
        status.setStatusMessage(statusMessage);
        return status;
    }

    /**
     * // method to send log out response to IDP
     * 
     * @param status
     * @param nameID
     * @param response
     */
    private void sendLogoutResponseToIDP(Status status, String nameID, HttpServletResponse response) {
        LogoutResponseBuilder logoutResponseGenerator = new LogoutResponseBuilder();

        LogoutResponse logoutResponse = logoutResponseGenerator.buildObject(LogoutResponse.DEFAULT_ELEMENT_NAME);
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

        logoutResponse.setID(UUID.randomUUID().toString());
        logoutResponse.setIssuer(getIssuer(builderFactory));
        logoutResponse.setVersion(SAMLVersion.VERSION_20);
        logoutResponse.setIssueInstant(new DateTime());

        System.out.println("****** Sending log out response to IDP *******");
        // setting the name ID as in response to parameter, have to be checked
        logoutResponse.setInResponseTo(nameID);
        String destination = properties.getProperty("DefaultIdP.logout.response.location");// "http://localhost:7001/sso-web/singleLogoutResponse.action";
        logoutResponse.setDestination(destination);
        logoutResponse.setStatus(status);

        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        try {
            sendLogoutMsg(response, builderFactory, destination, responseLocation, logoutResponse);
        } catch (MessageEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method decodes the Log out response sent by the IDP.
     */
    public void getLogoutResponse() {
        ServletActionContext.getRequest();
        SAMLMessageContext messageContext;
        try {
            messageContext = extractSAMLMessageContext(ServletActionContext.getRequest());

            System.out.println("after getting the Message Context");
            System.out.println("before log out response cast");
            LogoutResponse logoutResponse = (LogoutResponse) messageContext.getInboundSAMLMessage();
            System.out.println("after log out response cast");

            if (logoutResponse.getIssuer() != null) {
                Issuer issuer = logoutResponse.getIssuer();
                System.out.println("issuer fo log out response is :" + issuer.getValue());
            }

            // Verify status
            statusCode = logoutResponse.getStatus().getStatusCode().getValue();
            addActionMessage("Log out status Code Obtained =" + statusCode);

            if (StatusCode.SUCCESS_URI.equals(statusCode)) {
                System.out.println("status code = " + statusCode);
                LOG.trace("Single Logout was successful");
                addActionMessage("Single Logout was successful");

            } else if (StatusCode.PARTIAL_LOGOUT_URI.equals(statusCode)) {
                System.out.println("status code = " + statusCode);
                LOG.trace("Single Logout was partially successful");
                addActionMessage("Single Logout was partially successful");
            } else {
                String[] logMessage = new String[2];
                logMessage[0] = logoutResponse.getStatus().getStatusCode().getValue();
                StatusMessage message1 = logoutResponse.getStatus().getStatusMessage();
                if (message1 != null) {
                    logMessage[1] = message1.getMessage();
                }
                LOG.warn("Received LogoutResponse has invalid status code");
                addActionMessage("Single Logout was not completed");

            }
        } catch (MessageDecodingException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method extracts the SAML message context.
     * 
     * @param request
     * @return
     * @throws MessageDecodingException
     * @throws SecurityException
     */
    private SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request) throws MessageDecodingException,
            SecurityException {
        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
        SAMLMessageDecoder decoder = new HTTPPostSimpleSignDecoder();
        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
        decoder.decode(messageContext);
        return messageContext;
    }

    /**
     * This method initiates sending a log out request to the IDP.
     * 
     * @param response
     * @param subNameID
     * @throws MessageEncodingException
     */
    private void sendLogoutRequest(HttpServletResponse response, String subNameID) throws MessageEncodingException {
        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        String destination = properties.getProperty("DefaultIdP.logout.location");// "http://localhost:7001/sso-web/singleLogout.action";
        LogoutRequest logoutRequest = generateLogoutRequest(destination, responseLocation, builderFactory, subNameID);
        sendLogoutMsg(response, builderFactory, destination, responseLocation, logoutRequest);

    }

    /**
     * This method send the log out request message across to the IDP.
     * 
     * @param response
     * @param builderFactory
     * @param destination
     * @param responseLocation2
     * @param logoutRequest
     * @throws MessageEncodingException
     */
    private void sendLogoutMsg(HttpServletResponse response, XMLObjectBuilderFactory builderFactory,
            String destination, String responseLocation2, SignableSAMLObject logoutRequest)
            throws MessageEncodingException {
        Properties p = new Properties();
        p.setProperty("resource.loader", "classpath");
        p.setProperty("classpath.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        /* first, get and initialize an engine */
        VelocityEngine velocityEngine = new VelocityEngine();
        try {
            velocityEngine.init(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServletResponseAdapter outTransport = new HttpServletResponseAdapter(response, false);
        BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
        SAMLMessageEncoder encoder =
                new HTTPPostSimpleSignEncoder(velocityEngine, "/templates/saml2-post-binding.vm", true);
        messageContext.setOutboundMessageTransport(outTransport);
        messageContext.setPeerEntityEndpoint(generateEndpoint(builderFactory,
                AssertionConsumerService.DEFAULT_ELEMENT_NAME, destination, responseLocation));
        messageContext.setOutboundSAMLMessage(logoutRequest);
        messageContext.setOutboundMessageIssuer(issuerURL);
        // messageContext.setOutboundSAMLMessageSigningCredential(getCredential());
        messageContext.setRelayState("stateFromSP");
        System.out.println("before calling Logout request encoder.encode()%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        encoder.encode(messageContext);

    }

    /**
     * Generates Signature Credential
     * 
     * @return Credential
     */
    public Credential getCredential() {
        BasicX509Credential credential = new BasicX509Credential();
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = "testpwd".toCharArray();
            java.io.FileInputStream fis = new java.io.FileInputStream((String) properties.get("CertificateLocation"));
            keyStore.load(fis, password);
            fis.close();

            KeyStore.PrivateKeyEntry pkEntry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry("testsender", new KeyStore.PasswordProtection(
                            "send123".toCharArray()));
            PrivateKey pk = pkEntry.getPrivateKey();
            X509Certificate certificate = (X509Certificate) pkEntry.getCertificate();
            credential.setEntityCertificate(certificate);
            credential.setPublicKey(certificate.getPublicKey());
            credential.setPrivateKey(pk);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return credential;
    }

    /**
     * It generates log out request.
     * 
     * @param destination
     * @param responseLocation
     * @param builderFactory
     * @param subNameID
     * @return
     */
    private LogoutRequest generateLogoutRequest(String destination, String responseLocation,
            XMLObjectBuilderFactory builderFactory, String subNameID) {

        LogoutRequestBuilder logoutRequestBuilder =
                (LogoutRequestBuilder) builderFactory.getBuilder(LogoutRequest.DEFAULT_ELEMENT_NAME);

        LogoutRequest logoutRequest = logoutRequestBuilder.buildObject();

        logoutRequest.setID(UUID.randomUUID().toString());
        logoutRequest.setIssueInstant(new DateTime());
        logoutRequest.setDestination(destination);
        logoutRequest.setIssuer(getIssuer(builderFactory));
        // authnRequest.setRequestedAuthnContext(arg0);
        // authnRequest.setForceAuthn(Boolean.TRUE);

        NameIDBuilder nameIDBuilder = (NameIDBuilder) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
        NameID nameID = nameIDBuilder.buildObject();
        nameID.setValue(subNameID);
        nameID.setFormat(NameIDType.PERSISTENT);

        if (subNameID != null && subNameID != "") {
            logoutRequest.setNameID(nameID);
        }

        System.out.println("session index before setting in SP =" + subNameID);

        SAMLObjectBuilder<SessionIndex> sessionIndexBuilder =
                (SAMLObjectBuilder<SessionIndex>) builderFactory.getBuilder(SessionIndex.DEFAULT_ELEMENT_NAME);

        SessionIndex index = sessionIndexBuilder.buildObject();
        index.setSessionIndex(subNameID);
        logoutRequest.getSessionIndexes().add(index);

        return logoutRequest;
    }

    /**
     * Generates Issuer.
     * 
     * @param builderFactory
     * @return
     */
    private Issuer getIssuer(XMLObjectBuilderFactory builderFactory) {
        IssuerBuilder issuerBuilder = (IssuerBuilder) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
        Issuer issuer = issuerBuilder.buildObject();
        issuer.setValue(issuerURL);
        issuer.setFormat(NameIDType.ENTITY);
        return issuer;
    }

    /**
     * Generates the end point.
     * 
     * @param builderFactory
     * @param service
     * @param location
     * @param responseLocation
     * @return
     */
    @SuppressWarnings("unchecked")
    private Endpoint generateEndpoint(XMLObjectBuilderFactory builderFactory, QName service, String location,
            String responseLocation) {
        System.out.println("end point service: {}" + service);
        System.out.println("end point location: {}" + location);
        System.out.println("end point responseLocation: {}" + responseLocation);
        SAMLObjectBuilder<Endpoint> endpointBuilder = (SAMLObjectBuilder<Endpoint>) builderFactory.getBuilder(service);
        Endpoint samlEndpoint = endpointBuilder.buildObject();
        samlEndpoint.setLocation(location);
        // this does not have to be set
        if (responseLocation != null && !responseLocation.equals("")) {
            samlEndpoint.setResponseLocation(responseLocation);
        }
        return samlEndpoint;
    }

    /**
     * @param idpAddress
     *            the idpAddress to set
     */
    public void setIdpAddress(String idpAddress) {
        this.idpAddress = idpAddress;
    }

    /**
     * @return the idpAddress
     */
    public String getIdpAddress() {
        return idpAddress;
    }

}
