package net.es.oscars.meican.server;

import net.es.oscars.api.soap.gen.v06.*;
import net.es.oscars.client.OSCARSClient;
import net.es.oscars.client.OSCARSClientConfig;
import net.es.oscars.client.OSCARSClientException;
import net.es.oscars.common.soap.gen.MessagePropertiesType;
import net.es.oscars.common.soap.gen.OSCARSFaultMessage;
import net.es.oscars.common.soap.gen.OSCARSFaultReport;
import net.es.oscars.topoBridge.soap.gen.GetTopologyRequestType;
import net.es.oscars.topoBridge.soap.gen.GetTopologyResponseType;
import net.es.oscars.utils.clients.TopoBridgeClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.ogf.schema.network.topology.ctrlplane.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
* @author Jeremy, based on v0.5 counterpart by
* @author pfbiasuz
* 
* This class serves as the middle-man between MEICAN and OSCARSv0.6. 
* Specifically, it provides connections to:
* 	- OSCARSDriver06.php on MEICAN side, 
* 	- OSCARSClient.java on OSCARS side
**/



@WebService(targetNamespace = "http://server.meican.oscars.es.net/", portName = "OSCARSBridgePort", serviceName = "OSCARSBridgeWebService")
public class OSCARSBridgeWebService
{  
    private static OSCARSClient oscarsClient;
    
    /**************************************************************************
    * Handles WS-Security connections and instantiates/connects to an instance of OSCARSClient
    * 
    * @param oscars_url, where the instance of OSCARSv0.6 is running: Likely @ http://localhost:9001/OSCARS.
    * @return true is successful connection is established, false otherwise.
    **************************************************************************/
    @WebMethod //@Override
    public @WebResult(name = "return")boolean buildBridge(@WebParam(name = "oscarsUrl")String oscarsUrl)
    {    	
    	try
        {
    		//Set up keystores -- Will have to change this per system -- might be able to rewrite to accept this info from MEICAN admin
    		OSCARSClientConfig.setClientKeystore("mykey", "/Users/jplante/OSCARS_HOME/sampleDomain/certs/client.jks", "changeit");
    		OSCARSClientConfig.setSSLKeyStore("/Users/jplante/OSCARS_HOME/sampleDomain/certs/client.jks", "changeit");
    		
    		System.out.println("* Building Bridge to: " + oscarsUrl);
    		
    		oscarsClient = new OSCARSClient(oscarsUrl, oscarsUrl + "?wsdl");	// Connect to OSCARS
            java.util.logging.Logger.getLogger("org.springframework.beans.factory").setLevel(java.util.logging.Level.INFO);
            java.util.logging.Logger.getLogger("org.apache.cxf").setLevel(java.util.logging.Level.INFO);
    		return true;
        }
        catch(OSCARSClientException ce) 
        {
        	System.out.println("OSCARSClientException thrown trying to initialize OSCARSClient");
        	ce.printStackTrace();
        }
    	catch(Exception e)
    	{
    		System.out.println("Exception thrown trying to initialize OSCARSClient");
    		e.printStackTrace();
    	}
    	    	
    	return false;
    }
    
    /**************************************************************************
    * Invokes a createReservation in remote OSCARSClient.java
    * 
    * @param oscars_url
    * @param description
    * @param srcEndpoint
    * @param srcIsTagged
    * @param srcTag
    * @param destEndpoint
    * @param destIsTagged
    * @param destTag
    * @param path
    * @param bandwidth
    * @param pathSetupMode
    * @param startTimestamp
    * @param endTimestamp
    * @Precondition All parameters must be non-null. This should be taken care of by higher-level MEICAN calling functions, so it is NOT handled here.
    * @Postcondition ResCreateContent object built and passed to OSCARSv0.6 for a createReservation() request. OSCARS will return a CreateReply with status and assigned GRI, and this request will be added to OSCARS databases as appropriate.
    * @return reply, String representation of OSCARSv0.6 createReservation() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> createReservation(	
    											@WebParam(name = "description")String description, @WebParam(name = "srcEndpoint")String srcEndpoint, @WebParam(name = "srcIsTagged")String srcIsTagged, 
    											@WebParam(name = "srcTag")String srcTag, @WebParam(name = "destEndpoint")String destEndpoint, @WebParam(name = "destIsTagged")String destIsTagged, 
    											@WebParam(name = "destTag")String destTag, @WebParam(name = "path")String path, @WebParam(name = "bandwidth")int bandwidth, @WebParam(name = "pathSetupMode")String pathSetupMode, 
    											@WebParam(name = "startTimestamp")long startTimestamp, @WebParam(name = "endTimestamp")long endTimestamp) 
    {       
    	System.out.println("- Creating new Reservation");
    	
        boolean hasEro = false;
       	
    	/**
        * ResCreateContent					-->	GRI, Description
        * - UserRequestConstraintType		-->	Start Time, End Time, Bandwidth
        * -- PathInfo						-->	Path Setup Mode, Path Type
        * --- CtrlPlanePathContent 			-->	List<Hops>
        * ---- CtrlPlaneHopContent			-->	Domain, Node, Port, Link
        * --- Layer2Info					-->	Src Endpoint, Dst Endpoint
        * ---- VlanTag						-->	VLAN value, IsTagged?
        **/
        ResCreateContent request = new ResCreateContent();
        UserRequestConstraintType userConstraint = new UserRequestConstraintType();
        PathInfo pathInfo = new PathInfo();
        CtrlPlanePathContent pathContent = new CtrlPlanePathContent();
        Layer2Info layer2Info = new Layer2Info();
        VlanTag srcVtag = new VlanTag();
        VlanTag destVtag = new VlanTag();
        
        // Set src VLAN Parameters
        if (srcIsTagged.equals("true")) 
        {
            srcVtag.setTagged(true);
            srcVtag.setValue(srcTag);
        } 
        else 
        {
            srcVtag.setTagged(false);
            srcVtag.setValue("any");
        }
        
        
        // Set dst VLAN Parameters
        if (destIsTagged.equals("true")) 
        {
            destVtag.setTagged(true);
            destVtag.setValue(destTag);
        } 
        else 
        {
            destVtag.setTagged(false);
            destVtag.setValue("any");
        }
        
        // Add src/dst VLANs to Layer2Info
        layer2Info.setSrcVtag(srcVtag);
        layer2Info.setDestVtag(destVtag);
        
        // Complete Layer2Info population
        layer2Info.setSrcEndpoint(srcEndpoint);
        layer2Info.setDestEndpoint(destEndpoint); 
        
        // Add Layer2Info to PathInfo
        pathInfo.setLayer2Info(layer2Info);

        // Complete PathInfo population
        pathInfo.setPathSetupMode(pathSetupMode);

        
        // Parse path to get {domain:node:port:link} for each hop (src/dst)
        if (!path.equals("null")) 
        {
            String[] hops = path.split(";");
            
            hasEro = true;
            pathContent.setId("userPath");
            
            for (int i = 0; i < hops.length; i++) 
            {
                String hopId = hops[i];
                
                if (hopId != null) 
                {
                	int hopType;
                	CtrlPlaneHopContent hop = new CtrlPlaneHopContent();
                	
                    hopId = hopId.trim();
                    hopType = hopId.split(":").length;
                    hasEro = true;
                    
                    hop.setId(i + "");
                    if (hopType == 4) 
                    {
                        hop.setDomainIdRef(hopId);
                    } 
                    else if (hopType == 5) 
                    {
                        hop.setNodeIdRef(hopId);
                    } 
                    else if (hopType == 6) 
                    {
                        hop.setPortIdRef(hopId);
                    } 
                    else 
                    {
                        hop.setLinkIdRef(hopId);
                    }
                    
                    pathContent.getHop().add(hop);	//Add hop to list of hops in CtrlPlanePathContent
                }
            }
            if (hasEro) 
            {
                pathInfo.setPath(pathContent);
            }
        }
        
        // Add PathInfo to UserRequestConstraint
        userConstraint.setPathInfo(pathInfo);
        
        // Complete UserRequestConstraint population
        userConstraint.setStartTime(startTimestamp);
        userConstraint.setEndTime(endTimestamp);
        userConstraint.setBandwidth(bandwidth);

        // Add UserRequestConstraint to ResCreateContent
        request.setUserRequestConstraint(userConstraint);
        
        // Complete ResCreateContent population
        request.setDescription(description);

    
        /* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
        
        String message;
        ArrayList<String> reply = new ArrayList<String>();
        String gri;
        String status;        
        CreateReply response;		//This is what comes back from OSCARS
                        
        try 
        {
            response = oscarsClient.createReservation(request);		// Submit createReservation request to OSCARS and get response back
            gri = response.getGlobalReservationId();
            status = response.getStatus();

            System.out.println("Reservation GRI: " + gri);
            System.out.println("Initial Status: " + status);

            reply.add(gri);
            reply.add(status);
            reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }

        return reply;
    }

    
    /**************************************************************************
    * Invokes a queryReservation in remote OSCARSClient.java 
    *
    * @param oscars_url
    * @param gri
    * @return reply, String representation of OSCARSv0.6 queryReservation() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> queryReservation(@WebParam(name = "gri")String gri) 
    {    	
    	System.out.println("- Querying GRI: " + gri);
    	
    	QueryResContent query = new QueryResContent();   	
    	query.setGlobalReservationId(gri);
    	
    	/* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
    	
        ArrayList<String> reply = new ArrayList<String>();
        String message;
        
    	/**
        * QueryResReply						-->	List<OSCARSFaultReports>
        * - ResDetails						-->	GRI, Login, Description, Create Time, Status
        * -- ReservedConstraintType			--> Start Time, End Time, Bandwidth
        * --- PathInfo						-->	Path Setup Mode, Path Type
        * ---- CtrlPlanePathContent 		-->	List<Hops>
        * ----- CtrlPlaneHopContent			-->	Domain, Node, Port, Link
        * ---- Layer2Info					-->	Src Endpoint, Dst Endpoint
        * ----- VlanTag						-->	VLAN value, IsTagged?
        * -- UserRequestConstraintType		-->	Start Time, End Time, Bandwidth
        * --- PathInfo						-->	Path Setup Mode, Path Type
        * ---- CtrlPlanePathContent 		-->	List<Hops>
        * ----- CtrlPlaneHopContent			-->	Domain, Node, Port, Link
        * ---- Layer2Info					-->	Src Endpoint, Dst Endpoint
        * ----- VlanTag						-->	VLAN value, IsTagged?
        * - OSCARSFaultReports				--> Error Code, Error Type, Error Message
        **/
         
        QueryResReply response;									//This is what comes back from OSCARS
        ResDetails responseDetails = null;
        ReservedConstraintType reservedConstraints;
        UserRequestConstraintType requestConstraints;
        PathInfo pathInfo;
        CtrlPlanePathContent path;
        Layer2Info layer2Info;
        VlanTag srcVtag;
        String srcVlan = "null";
        Boolean isSrcTagged = false;
        VlanTag destVtag;
        String destVlan = "null";
        Boolean isDstTagged = false;
        String startTime;
        String endTime;
        String bandwidth;
        
        List<OSCARSFaultReport> oscarsFaults;
        
        try
        {
	        response = oscarsClient.queryReservation(query);		// Submit queryReservation request to OSCARS and get response back
	        	        	        
	        responseDetails = response.getReservationDetails();
	        System.out.println("Status = " + responseDetails.getStatus());
	        
	        reservedConstraints = responseDetails.getReservedConstraint();
	        requestConstraints = responseDetails.getUserRequestConstraint();
	        
	        if(reservedConstraints == null)		 // Not Yet Reserved
	        {
	        	startTime = String.valueOf(requestConstraints.getStartTime());
	        	endTime = String.valueOf(requestConstraints.getEndTime());
	        	bandwidth = String.valueOf(requestConstraints.getBandwidth());
	        	
	        	pathInfo = requestConstraints.getPathInfo();
	        }
	        else								// Already Reserved
	        {
	        	startTime = String.valueOf(reservedConstraints.getStartTime());
	        	endTime = String.valueOf(reservedConstraints.getEndTime());
	        	bandwidth = String.valueOf(reservedConstraints.getBandwidth());
	        	
	        	pathInfo = reservedConstraints.getPathInfo();
	        }
	        
	        path = pathInfo.getPath();
	        layer2Info = pathInfo.getLayer2Info();
	
	        if (layer2Info != null) 
	        {
	        	// Get Src VLAN info
	            srcVtag = layer2Info.getSrcVtag();
	            
	            if(srcVtag != null)
	            {
	            	srcVlan = srcVtag.getValue();
	            	isSrcTagged = srcVtag.isTagged();          
	            }
	            
	            // Get Dst VLAN info
	            destVtag = layer2Info.getDestVtag();
	            
	            if(destVtag != null)
	            {
	            	destVlan = destVtag.getValue();
	            	isDstTagged = destVtag.isTagged();
	            }
	        }
	
	        // Get URN info for every hop (Link IDs)
	        StringBuilder pathString = new StringBuilder();
	
	        for (CtrlPlaneHopContent hop : path.getHop()) 
	        {
	            CtrlPlaneLinkContent link = hop.getLink();
	            
	            if (link == null)	//should not happen 
	            {
	                pathString.append("no link" + ";");
	                
	                continue;
	            }
	            
	            pathString.append(link.getId() + ";");
	        }
	
	        // Populate the reply message to MEICAN
	        reply.add(responseDetails.getGlobalReservationId());
	        reply.add(responseDetails.getStatus());
	        reply.add(responseDetails.getDescription());
	        reply.add(responseDetails.getLogin());
	        reply.add(String.valueOf(responseDetails.getCreateTime()));
	        reply.add(startTime);
	        reply.add(endTime);
	        reply.add(bandwidth);
	        reply.add(pathInfo.getPathSetupMode());
	        reply.add(layer2Info.getSrcEndpoint());
	        reply.add(isSrcTagged.toString());
	        reply.add(srcVlan);
	        reply.add(layer2Info.getDestEndpoint());
	        reply.add(isDstTagged.toString());
	        reply.add(destVlan);
	        reply.add(pathString.toString());
	        reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        
        return reply;
    }

    /**************************************************************************
    * Invokes a cancelReservation in remote OSCARSClient.java 
    *
    * @param oscars_url
    * @param gri
    * @return reply, String representation of OSCARSv0.6 cancelReservation() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> cancelReservation(@WebParam(name = "gri")String gri) 
    {    	
    	System.out.println("- Cancelling GRI: " + gri);
    	
    	CancelResContent cancelRequest = new CancelResContent();   	
    	cancelRequest.setGlobalReservationId(gri);
    	
        /* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
    	ArrayList<String> reply = new ArrayList<String>();
    	String message;
    	
    	/**
        * CancelResReply						-->	GRI, Status
        **/
        CancelResReply cancelResponse;										//This is what comes back from OSCARS
        String status;
        
        try
        {
        	cancelResponse = oscarsClient.cancelReservation(cancelRequest);		// Submit queryReservation request to OSCARS and get response back
        	status = cancelResponse.getStatus();
        
        	System.out.println("Cancellation complete");
        	
        	reply.add(gri);
        	reply.add(status);
        	reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        
        return reply;
    }

    
    /**************************************************************************
    * Invokes a series of queryReservation calls in remote OSCARSClient.java 
    * 
    * @param oscars_url
    * @param grisString, String representation of specific GRIs to be listed, individual GRIs separated by semicolons
    * @return reply, String representation of statuses for each requested GRI in list
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> listReservations(@WebParam(name = "grisString")String grisString) 
    {    	
    	String[] gris = grisString.split(";");     
    	    	
    	ArrayList<String> oneQueryResponse;
        ArrayList<String> reply = new ArrayList<String>();
        

        for (int i = 0; i < gris.length; i++) 
        {        	
            String oneStatus = new String();
            
        	oneQueryResponse = queryReservation(gris[i]);	//Query each requested GRI individually
        	
        	if(oneQueryResponse.size() < 3)		// Query resulted in SOAPFaultMessage
        		oneStatus = (String)oneQueryResponse.get(0);
        	else
        		oneStatus = (String)oneQueryResponse.get(2);
        	
            reply.add(oneStatus);            
        }
        reply.add(0, "");
        
        return reply;
    }

    
    /**************************************************************************
    * Invokes a modifyReservation call in remote OSCARSClient.java 
    * 
    * @param oscars_url
    * @param gri
    * @param startTimestamp
    * @param endTimestamp
    * @return reply, String representation of OSCARSv0.6 modifyReservation() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> modifyReservation(@WebParam(name = "gri")String gri, @WebParam(name = "startTimestamp")long startTimestamp, @WebParam(name = "endTimestamp")long endTimestamp) 
    {    	
    	ModifyResContent modifyRequest = new ModifyResContent();
    	UserRequestConstraintType userConstraints = new UserRequestConstraintType();
    	
    	userConstraints.setStartTime(startTimestamp);
    	userConstraints.setEndTime(endTimestamp);
    	//userConstraints.setBandwidth(100);
    	
    	modifyRequest.setGlobalReservationId(gri);
    	//modifyRequest.setDescription("nao sera alterada");
    	modifyRequest.setUserRequestConstraint(userConstraints);
        
        
        /* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
        ArrayList<String> reply = new ArrayList<String>();
        String message;
        
        /**
         * ModifyResReply						-->	GRI, Status
         **/
        ModifyResReply response;					//This is what comes back from OSCARS

        try
        {
	        response = oscarsClient.modifyReservation(modifyRequest);		// Submit modifyReservation request to OSCARS and get response back
	        
	        reply.add(response.getGlobalReservationId());
	        reply.add(response.getStatus());
	        reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        
        return reply;
    }

    
    /**************************************************************************
    * Invokes a createPath call in remote OSCARSClient.java 
    * 
    * @param oscars_url
    * @param gri
    * @return reply, String representation of OSCARSv0.6 createPath() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> createPath(@WebParam(name = "gri")String gri) 
    {            	
        CreatePathContent createRequest = new CreatePathContent();
        createRequest.setGlobalReservationId(gri);
        
    	/* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
    	ArrayList<String> reply = new ArrayList<String>();
    	String message;

    	/**
         * CreatePathResponseContent			-->	GRI, Status
         **/
        CreatePathResponseContent createResponse;			// This is what comes back from OSCARS
        
        try
        {
	        createResponse = oscarsClient.createPath(createRequest);		// Submit createPath request to OSCARS and get response back
	
	        reply.add(createResponse.getGlobalReservationId());
	        reply.add(createResponse.getStatus());
	        reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }

        return reply;
    }

    
    /**************************************************************************
    * Invokes a teardownPath call in remote OSCARSClient.java 
    * 
    * @param oscars_url
    * @param gri
    * @return reply, String representation of OSCARSv0.6 teardownPath() response object.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> teardownPath(@WebParam(name = "gri")String gri) 
    {    	
    	TeardownPathContent teardownRequest = new TeardownPathContent();	
        teardownRequest.setGlobalReservationId(gri);
        
        /* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
    	ArrayList<String> reply = new ArrayList<String>();
        String message;
        
        /**
         * TeardownPathResponseContent			-->	GRI, Status
         **/
        TeardownPathResponseContent teardownResponse; 		// This is what comes back from OSCARS
        
        try
        {
	        teardownResponse = oscarsClient.teardownPath(teardownRequest);		// Submit teardownPath request to OSCARS and get response back
	        
	        reply.add(teardownResponse.getGlobalReservationId());
	        reply.add(teardownResponse.getStatus());
	        reply.add(0, "");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }

        return reply;
    }

    /**************************************************************************
    * Invokes a listReservations call in remote OSCARSClient.java  
    * @param oscars_url
    * @param status
    * @return reply, String representation of OSCARSv0.6 listReservations() response object.
    * 
    * NOTE: This isn't called anywhere in MEICAN presently, so may need to modify reply ArrayList to include Layer3Info and MplsInfo, and maybe add delimeters between GRIs.
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> listAllReservations(@WebParam(name = "status")String status) 
    {                   	
        ListRequest listRequest = new ListRequest();
        listRequest.getResStatus().add(status);				//Add status to list of statuses in ListRequest
        
    	/* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
        ArrayList<String> reply = new ArrayList<String>();
        String message;      
        
    	/**
        * ListReply						-->	List<ResDetails>
        * - ResDetails					-->	GRI, Login, Description, Create Time, Status
        * -- ReservedConstraintType		--> Start Time, End Time, Bandwidth
        * --- PathInfo					-->	Path Setup Mode, Path Type
        * ---- Layer2Info				-->	Src Endpoint, Dst Endpoint
        * ----- VlanTag					-->	VLAN value, IsTagged?
        **/
        ListReply listResponse;
        List<ResDetails> allDetails;
        ReservedConstraintType reservedConstraint;
        PathInfo pathInfo;
        Layer2Info layer2Info;
        VlanTag srcVtag;
        VlanTag destVtag;
        
        try
        {
	        listResponse = oscarsClient.listReservations(listRequest);		// Submit listReservations request to OSCARS and get response back
	        allDetails = listResponse.getResDetails();
	        
	        for(ResDetails detail : allDetails) 
	        {
	        	reservedConstraint = detail.getReservedConstraint();
	            pathInfo = reservedConstraint.getPathInfo();
	            layer2Info = pathInfo.getLayer2Info();
	            
	            String startTime = String.valueOf(reservedConstraint.getStartTime());
	            String endTime = String.valueOf(reservedConstraint.getEndTime());
	            String bandwidth = String.valueOf(reservedConstraint.getBandwidth());
	            String srcVlan = "null";
	            String destVlan = "null";
	
	            reply.add(detail.getGlobalReservationId());
	            reply.add(startTime);
	            reply.add(endTime);
	            reply.add(bandwidth);
	            reply.add(detail.getDescription());
	
	            if (layer2Info != null) 
	            {
	            	srcVtag = layer2Info.getSrcVtag();
	            	destVtag = layer2Info.getDestVtag();
	                
	            	if(srcVtag != null)
	            		srcVlan = srcVtag.getValue();
	            	
	            	if(destVtag != null)
	            		destVlan = destVtag.getValue();
	
	                reply.add(layer2Info.getSrcEndpoint());
	                reply.add(layer2Info.getDestEndpoint());
	                reply.add(srcVlan);
	                reply.add(destVlan);
	            }
	        }
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (OSCARSClientException ce)
        {
            ce.printStackTrace();
            message = ce.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        
        return reply;
    }
    

    /**************************************************************************
    * Invokes a getTopology call in remote OSCARS.
    * However, this function is NO LONGER SUPPORTED BY THE API!!!!!
    * Therefore, must establish a  connection to the TopoBridge service client directly, and invoke the call there.
    * @param topoBridge_url, address of TopoBridge wsdl
    * @param topologyID, Domain ID of the desired topology
    * @return reply, String representation of topology corresponding to domain with id = topologyID
    **************************************************************************/
    @WebMethod
    public @WebResult(name = "return")ArrayList<String> getTopology(@WebParam(name = "topoBridge_url")String topoBridge_url, @WebParam(name = "topologyID")String topologyID) 
    {    	
    	/* getTopology() no longer supported by API, must talk to TopoBridgeClient directly! */
    	TopoBridgeClient topoBridge = null;
    	
    	try
    	{    		
    	  	topoBridge = TopoBridgeClient.getClient(topoBridge_url);
    	}
    	catch(Exception e)
    	{ 
    		System.out.println("Problem Connecting to TopoBridgeClient"); 
    	}
    	/**/
    	
    	GetTopologyRequestType topologyRequest = new GetTopologyRequestType();
    	topologyRequest.getDomainId().add(topologyID);
    	
    	// This is necessary to prevent Null-Pointer Exception //
    	MessagePropertiesType mt = new MessagePropertiesType();
    	mt.setGlobalTransactionId("made-up");
    	topologyRequest.setMessageProperties(mt);
    	// //
    	        
        /* REQUEST CONSTRUCTION COMPLETE, EVERYTHING AFTER THIS INVOLVES GETTING RESPONSE FROM OSCARS */
        ArrayList<String> reply = new ArrayList<String>();
        String message;
        String temp;
        
        /**
         * GetTopologyResponseType					-->	List<CtrlPlaneTopologyContent>
         * - CtrlPlaneTopologyContent				--> List<CtrlPlaneDomainContent>
         * -- CtrlPlaneDomainContent				--> List<CtrlPlaneNodeContent>
         * --- CtrlPlaneNodeContent					--> List<CtrlPlanePortContent>
         * ---- CtrlPlanePortContent				--> List<CtrlPlaneLinkContent>, Port ID, Capacity, Granularity, Minimum Reservable Capacity, Maximum Reservable Capacity 
         * ----- CtrlPlaneLinkContent				--> Link ID, Remote Link ID, Capacity, Granularity, Minimum Reservable Capacity, Maximum Reservable Capacity, VLAN Range
         **/
        GetTopologyResponseType topologyResponse;					//This is what comes back from OSCARS
        List<CtrlPlaneTopologyContent> allTopologies;
        List<CtrlPlaneDomainContent> allDomains;
        List<CtrlPlaneNodeContent> allNodesInDomain;
        List<CtrlPlanePortContent> allPortsOnNode;
        List<CtrlPlaneLinkContent> allLinksOnPort;

        try
        {
        	topologyResponse = topoBridge.getPortType().getTopology(topologyRequest);	// Submit getTopology request to OSCARS and get response back
	        
        	System.out.println("TOPOLOGY GOTTEN");
        	
	        allTopologies = topologyResponse.getTopology();
	        allDomains = allTopologies.get(0).getDomain();

	        for (CtrlPlaneDomainContent oneDomain : allDomains) 
	        {
	        	temp = oneDomain.getId();
	            reply.add(temp);
	            
	            allNodesInDomain = oneDomain.getNode();
	            
	            for (CtrlPlaneNodeContent oneNode : allNodesInDomain) 
	            {
	                temp = oneNode.getId();
	                reply.add(temp);
	                
	                allPortsOnNode = oneNode.getPort();
	                
	                for (CtrlPlanePortContent onePort : allPortsOnNode) 
	                {
	                    temp = onePort.getId() + " " + onePort.getCapacity() + " " + onePort.getGranularity() + " " + onePort.getMinimumReservableCapacity() + " " + onePort.getMaximumReservableCapacity();
	                    reply.add(temp);
	                    
	                    allLinksOnPort = onePort.getLink();
	                    
	                    if (allLinksOnPort != null) 
	                    {
	                        for (CtrlPlaneLinkContent oneLink : allLinksOnPort) 
	                        {
	                            CtrlPlaneSwcapContent swcap = oneLink.getSwitchingCapabilityDescriptors();
	                            CtrlPlaneSwitchingCapabilitySpecificInfo swcapEsp = swcap.getSwitchingCapabilitySpecificInfo();
	                            
	                            String capacity;
	                            String granularity;
	                            String minReservable;
	                            String maxReservable;
	                            	                            
	                            // No Link capacity bounds set -- Use port-bounds as substitute //
	                            if(oneLink.getCapacity() == null)
	                            {
	                            	capacity = onePort.getCapacity();
	                            	granularity = onePort.getGranularity();
	                            	minReservable = onePort.getMinimumReservableCapacity();
	                            	maxReservable = onePort.getMaximumReservableCapacity();
	                            }
	                            else
	                            {
	                            	capacity = oneLink.getCapacity();
	                            	granularity = oneLink.getGranularity();
	                            	minReservable = oneLink.getMinimumReservableCapacity();
	                            	maxReservable = oneLink.getMaximumReservableCapacity();
	                            }

	                            temp = oneLink.getId() + " " + oneLink.getRemoteLinkId() + " " + capacity + " " + granularity + " " + minReservable + " " + maxReservable + " " + swcapEsp.getVlanRangeAvailability();
	                            reply.add(temp);
	                        }
	                    }
	                }
	            }
	        }
        
	        reply.add(0,"");
        }
        catch (OSCARSFaultMessage fm) 
        {
            fm.printStackTrace();
            message = fm.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            message = e.getMessage();
            reply.add(0, "Error: Exception (" + message + ")");
        }

        return reply;    	
    }
    
    
    //public ArrayList<String> refreshPath(String oscars_url, String gri){}		-----> NO LONGER SUPPORTED BY API in v0.6
    
}
