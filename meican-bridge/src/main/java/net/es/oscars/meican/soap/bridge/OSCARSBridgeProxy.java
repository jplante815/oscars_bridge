
package net.es.oscars.meican.soap.bridge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(targetNamespace = "http://server.meican.oscars.es.net/", wsdlLocation = "http://localhost:8080/OSCARSBridgeV6?wsdl")
public class OSCARSBridgeProxy
    extends Service
{

    private final static URL OSCARSBRIDGESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(OSCARSBridgeProxy.class.getName());
    private final static String namespace = "http://server.meican.oscars.es.net/";
    private final static QName port_location = new QName(namespace, "OSCARSBridgePort");
    private final static QName svc_location = new QName(namespace, "OSCARSBridgeWebService");

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = OSCARSBridgeProxy.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/OSCARSBridgeV6?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/OSCARSBridgeV6?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        OSCARSBRIDGESERVICE_WSDL_LOCATION = url;
    }

    // public OSCARSBridgeProxy(URL wsdlLocation, QName serviceName) {
       // super(wsdlLocation, serviceName);
    //}

    public OSCARSBridgeProxy() {
        super(OSCARSBRIDGESERVICE_WSDL_LOCATION, svc_location);
    }

    /**
     * 
     * @return
     *     returns OSCARSBridge
     */
    //@WebEndpoint(name = "OSCARSBridgeWebService")
    public OSCARSBridge getOSCARSBridgePort() {
        return super.getPort(port_location, OSCARSBridge.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OSCARSBridge
     */
    //@WebEndpoint(name = "OSCARSBridgeWebService")
    public OSCARSBridge getOSCARSBridgePort(WebServiceFeature... features) {
        return super.getPort(port_location, OSCARSBridge.class, features);
    }

}
