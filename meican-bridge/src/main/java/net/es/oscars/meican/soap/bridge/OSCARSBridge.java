
package net.es.oscars.meican.soap.bridge;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(targetNamespace = "http://server.meican.oscars.es.net/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface OSCARSBridge {


    /**
     * 
     * @param oscarsUrl
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "buildBridge", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.BuildBridge")
    @ResponseWrapper(localName = "buildBridgeResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.BuildBridgeResponse")
    public boolean buildBridge(
        @WebParam(name = "oscarsUrl", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg5
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @param arg11
     * @param arg10
     * @param arg6
     * @param arg7
     * @param arg8
     * @param arg9
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createReservation", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CreateReservation")
    @ResponseWrapper(localName = "createReservationResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CreateReservationResponse")
    public List<String> createReservation(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4,
        @WebParam(name = "arg5", targetNamespace = "")
        String arg5,
        @WebParam(name = "arg6", targetNamespace = "")
        String arg6,
        @WebParam(name = "arg7", targetNamespace = "")
        String arg7,
        @WebParam(name = "arg8", targetNamespace = "")
        int arg8,
        @WebParam(name = "arg9", targetNamespace = "")
        String arg9,
        @WebParam(name = "arg10", targetNamespace = "")
        long arg10,
        @WebParam(name = "arg11", targetNamespace = "")
        long arg11);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "queryReservation", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.QueryReservation")
    @ResponseWrapper(localName = "queryReservationResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.QueryReservationResponse")
    public List<String> queryReservation(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "cancelReservation", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CancelReservation")
    @ResponseWrapper(localName = "cancelReservationResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CancelReservationResponse")
    public List<String> cancelReservation(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listReservations", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ListReservations")
    @ResponseWrapper(localName = "listReservationsResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ListReservationsResponse")
    public List<String> listReservations(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "modifyReservation", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ModifyReservation")
    @ResponseWrapper(localName = "modifyReservationResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ModifyReservationResponse")
    public List<String> modifyReservation(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        long arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        long arg2);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createPath", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CreatePath")
    @ResponseWrapper(localName = "createPathResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.CreatePathResponse")
    public List<String> createPath(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "teardownPath", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.TeardownPath")
    @ResponseWrapper(localName = "teardownPathResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.TeardownPathResponse")
    public List<String> teardownPath(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listAllReservations", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ListAllReservations")
    @ResponseWrapper(localName = "listAllReservationsResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.ListAllReservationsResponse")
    public List<String> listAllReservations(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTopology", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.GetTopology")
    @ResponseWrapper(localName = "getTopologyResponse", targetNamespace = "http://server.meican.oscars.es.net/", className = "net.es.oscars.meican.soap.bridge.GetTopologyResponse")
    public List<String> getTopology(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

}
