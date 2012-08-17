
package net.es.oscars.meican.soap.bridge;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bridge package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryReservationResponse_QNAME = new QName("http://bridge/", "queryReservationResponse");
    private final static QName _ListAllReservationsResponse_QNAME = new QName("http://bridge/", "listAllReservationsResponse");
    private final static QName _ListAllReservations_QNAME = new QName("http://bridge/", "listAllReservations");
    private final static QName _ListReservationsResponse_QNAME = new QName("http://bridge/", "listReservationsResponse");
    private final static QName _BuildBridge_QNAME = new QName("http://bridge/", "buildBridge");
    private final static QName _ModifyReservationResponse_QNAME = new QName("http://bridge/", "modifyReservationResponse");
    private final static QName _QueryReservation_QNAME = new QName("http://bridge/", "queryReservation");
    private final static QName _GetTopology_QNAME = new QName("http://bridge/", "getTopology");
    private final static QName _CreatePathResponse_QNAME = new QName("http://bridge/", "createPathResponse");
    private final static QName _ModifyReservation_QNAME = new QName("http://bridge/", "modifyReservation");
    private final static QName _CancelReservationResponse_QNAME = new QName("http://bridge/", "cancelReservationResponse");
    private final static QName _CreatePath_QNAME = new QName("http://bridge/", "createPath");
    private final static QName _CreateReservationResponse_QNAME = new QName("http://bridge/", "createReservationResponse");
    private final static QName _BuildBridgeResponse_QNAME = new QName("http://bridge/", "buildBridgeResponse");
    private final static QName _GetTopologyResponse_QNAME = new QName("http://bridge/", "getTopologyResponse");
    private final static QName _CancelReservation_QNAME = new QName("http://bridge/", "cancelReservation");
    private final static QName _TeardownPathResponse_QNAME = new QName("http://bridge/", "teardownPathResponse");
    private final static QName _TeardownPath_QNAME = new QName("http://bridge/", "teardownPath");
    private final static QName _ListReservations_QNAME = new QName("http://bridge/", "listReservations");
    private final static QName _CreateReservation_QNAME = new QName("http://bridge/", "createReservation");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bridge
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BuildBridgeResponse }
     * 
     */
    public BuildBridgeResponse createBuildBridgeResponse() {
        return new BuildBridgeResponse();
    }

    /**
     * Create an instance of {@link ListReservations }
     * 
     */
    public ListReservations createListReservations() {
        return new ListReservations();
    }

    /**
     * Create an instance of {@link ListAllReservationsResponse }
     * 
     */
    public ListAllReservationsResponse createListAllReservationsResponse() {
        return new ListAllReservationsResponse();
    }

    /**
     * Create an instance of {@link ListAllReservations }
     * 
     */
    public ListAllReservations createListAllReservations() {
        return new ListAllReservations();
    }

    /**
     * Create an instance of {@link ListReservationsResponse }
     * 
     */
    public ListReservationsResponse createListReservationsResponse() {
        return new ListReservationsResponse();
    }

    /**
     * Create an instance of {@link BuildBridge }
     * 
     */
    public BuildBridge createBuildBridge() {
        return new BuildBridge();
    }

    /**
     * Create an instance of {@link ModifyReservation }
     * 
     */
    public ModifyReservation createModifyReservation() {
        return new ModifyReservation();
    }

    /**
     * Create an instance of {@link TeardownPathResponse }
     * 
     */
    public TeardownPathResponse createTeardownPathResponse() {
        return new TeardownPathResponse();
    }

    /**
     * Create an instance of {@link CreatePathResponse }
     * 
     */
    public CreatePathResponse createCreatePathResponse() {
        return new CreatePathResponse();
    }

    /**
     * Create an instance of {@link CancelReservation }
     * 
     */
    public CancelReservation createCancelReservation() {
        return new CancelReservation();
    }

    /**
     * Create an instance of {@link GetTopology }
     * 
     */
    public GetTopology createGetTopology() {
        return new GetTopology();
    }

    /**
     * Create an instance of {@link CreateReservationResponse }
     * 
     */
    public CreateReservationResponse createCreateReservationResponse() {
        return new CreateReservationResponse();
    }

    /**
     * Create an instance of {@link CancelReservationResponse }
     * 
     */
    public CancelReservationResponse createCancelReservationResponse() {
        return new CancelReservationResponse();
    }

    /**
     * Create an instance of {@link GetTopologyResponse }
     * 
     */
    public GetTopologyResponse createGetTopologyResponse() {
        return new GetTopologyResponse();
    }

    /**
     * Create an instance of {@link ModifyReservationResponse }
     * 
     */
    public ModifyReservationResponse createModifyReservationResponse() {
        return new ModifyReservationResponse();
    }

    /**
     * Create an instance of {@link QueryReservation }
     * 
     */
    public QueryReservation createQueryReservation() {
        return new QueryReservation();
    }

    /**
     * Create an instance of {@link CreatePath }
     * 
     */
    public CreatePath createCreatePath() {
        return new CreatePath();
    }

    /**
     * Create an instance of {@link QueryReservationResponse }
     * 
     */
    public QueryReservationResponse createQueryReservationResponse() {
        return new QueryReservationResponse();
    }

    /**
     * Create an instance of {@link CreateReservation }
     * 
     */
    public CreateReservation createCreateReservation() {
        return new CreateReservation();
    }

    /**
     * Create an instance of {@link TeardownPath }
     * 
     */
    public TeardownPath createTeardownPath() {
        return new TeardownPath();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "queryReservationResponse")
    public JAXBElement<QueryReservationResponse> createQueryReservationResponse(QueryReservationResponse value) {
        return new JAXBElement<QueryReservationResponse>(_QueryReservationResponse_QNAME, QueryReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllReservationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "listAllReservationsResponse")
    public JAXBElement<ListAllReservationsResponse> createListAllReservationsResponse(ListAllReservationsResponse value) {
        return new JAXBElement<ListAllReservationsResponse>(_ListAllReservationsResponse_QNAME, ListAllReservationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAllReservations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "listAllReservations")
    public JAXBElement<ListAllReservations> createListAllReservations(ListAllReservations value) {
        return new JAXBElement<ListAllReservations>(_ListAllReservations_QNAME, ListAllReservations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListReservationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "listReservationsResponse")
    public JAXBElement<ListReservationsResponse> createListReservationsResponse(ListReservationsResponse value) {
        return new JAXBElement<ListReservationsResponse>(_ListReservationsResponse_QNAME, ListReservationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuildBridge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "buildBridge")
    public JAXBElement<BuildBridge> createBuildBridge(BuildBridge value) {
        return new JAXBElement<BuildBridge>(_BuildBridge_QNAME, BuildBridge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModifyReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "modifyReservationResponse")
    public JAXBElement<ModifyReservationResponse> createModifyReservationResponse(ModifyReservationResponse value) {
        return new JAXBElement<ModifyReservationResponse>(_ModifyReservationResponse_QNAME, ModifyReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "queryReservation")
    public JAXBElement<QueryReservation> createQueryReservation(QueryReservation value) {
        return new JAXBElement<QueryReservation>(_QueryReservation_QNAME, QueryReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTopology }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "getTopology")
    public JAXBElement<GetTopology> createGetTopology(GetTopology value) {
        return new JAXBElement<GetTopology>(_GetTopology_QNAME, GetTopology.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePathResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "createPathResponse")
    public JAXBElement<CreatePathResponse> createCreatePathResponse(CreatePathResponse value) {
        return new JAXBElement<CreatePathResponse>(_CreatePathResponse_QNAME, CreatePathResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModifyReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "modifyReservation")
    public JAXBElement<ModifyReservation> createModifyReservation(ModifyReservation value) {
        return new JAXBElement<ModifyReservation>(_ModifyReservation_QNAME, ModifyReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "cancelReservationResponse")
    public JAXBElement<CancelReservationResponse> createCancelReservationResponse(CancelReservationResponse value) {
        return new JAXBElement<CancelReservationResponse>(_CancelReservationResponse_QNAME, CancelReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePath }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "createPath")
    public JAXBElement<CreatePath> createCreatePath(CreatePath value) {
        return new JAXBElement<CreatePath>(_CreatePath_QNAME, CreatePath.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "createReservationResponse")
    public JAXBElement<CreateReservationResponse> createCreateReservationResponse(CreateReservationResponse value) {
        return new JAXBElement<CreateReservationResponse>(_CreateReservationResponse_QNAME, CreateReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuildBridgeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "buildBridgeResponse")
    public JAXBElement<BuildBridgeResponse> createBuildBridgeResponse(BuildBridgeResponse value) {
        return new JAXBElement<BuildBridgeResponse>(_BuildBridgeResponse_QNAME, BuildBridgeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTopologyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "getTopologyResponse")
    public JAXBElement<GetTopologyResponse> createGetTopologyResponse(GetTopologyResponse value) {
        return new JAXBElement<GetTopologyResponse>(_GetTopologyResponse_QNAME, GetTopologyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "cancelReservation")
    public JAXBElement<CancelReservation> createCancelReservation(CancelReservation value) {
        return new JAXBElement<CancelReservation>(_CancelReservation_QNAME, CancelReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TeardownPathResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "teardownPathResponse")
    public JAXBElement<TeardownPathResponse> createTeardownPathResponse(TeardownPathResponse value) {
        return new JAXBElement<TeardownPathResponse>(_TeardownPathResponse_QNAME, TeardownPathResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TeardownPath }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "teardownPath")
    public JAXBElement<TeardownPath> createTeardownPath(TeardownPath value) {
        return new JAXBElement<TeardownPath>(_TeardownPath_QNAME, TeardownPath.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListReservations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "listReservations")
    public JAXBElement<ListReservations> createListReservations(ListReservations value) {
        return new JAXBElement<ListReservations>(_ListReservations_QNAME, ListReservations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bridge/", name = "createReservation")
    public JAXBElement<CreateReservation> createCreateReservation(CreateReservation value) {
        return new JAXBElement<CreateReservation>(_CreateReservation_QNAME, CreateReservation.class, null, value);
    }

}
