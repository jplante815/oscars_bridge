package OSCARSBridge;

import net.es.oscars.api.soap.gen.v06.*;
import net.es.oscars.client.OSCARSClient;
import net.es.oscars.client.OSCARSClientConfig;
import net.es.oscars.client.OSCARSClientException;
import net.es.oscars.common.soap.gen.OSCARSFaultMessage;
import net.es.oscars.common.soap.gen.OSCARSFaultReport;
import java.util.ArrayList;
import java.util.List;
import org.ogf.schema.network.topology.ctrlplane.*;

/**
* @author Jeremy, based on v0.5 counterpart by
* @author pfbiasuz
* 
* This class serves as the middle-man between MEICAN and OSCARSv0.6. 
* Specifically, it provides connections to:
* 	- OSCARSDriver06.php on MEICAN side, 
* 	- OSCARSClient.java on OSCARS side
**/
public class OSCARSBridge 
{  
    private OSCARSClient oscarsClient;	
    
    /**************************************************************************
    * Handles WS-Security connections and instantiates/connects to an instance of OSCARSClient
    * 
    * @param oscars_url, where the instance of OSCARSv0.6 is running: Likely @ http://localhost:9001/OSCARS.
    * @return true is successful connection is established, false otherwise.
    **************************************************************************/
    public boolean buildBridge(String oscars_url)
    {    	
    	try
        {
    		//Set up keystores 
    		OSCARSClientConfig.setClientKeystore("mykey", "/Users/jplante/OSCARS_HOME/sampleDomain/certs/client.jks", "changeit");
    		OSCARSClientConfig.setSSLKeyStore("/Users/jplante/OSCARS_HOME/sampleDomain/certs/oscars-ssl.jks", "changeit");
    		
    		oscarsClient = new OSCARSClient(oscars_url);	// Connect to OSCARS
    		
    		return true;
        }
        catch(OSCARSClientException ce) 
        {
        	System.out.println("Exception thrown trying to initialize OSCARSClient");
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
    * @param srcUrn
    * @param isSrcTagged
    * @param srcTag
    * @param destUrn
    * @param isDestTagged
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
    public ArrayList<String> createReservation(String description, String srcUrn, String isSrcTagged, String srcTag, String destUrn, String isDestTagged, String destTag, String path, int bandwidth, String pathSetupMode, long startTimestamp, long endTimestamp) 
    {       
        boolean hasEro = false;
        //String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5

       	
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
        if (isSrcTagged.equals("true")) 
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
        if (isDestTagged.equals("true")) 
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
        layer2Info.setSrcEndpoint(srcUrn);
        layer2Info.setDestEndpoint(destUrn); 
        
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

            System.out.println("GRI: " + gri);
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
    public ArrayList<String> queryReservation(String gri) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5
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
        * - OSCARSFaultReports				--> Error Code, Error Type, Error Message
        **/
         
        QueryResReply response;									//This is what comes back from OSCARS
        ResDetails responseDetails;
        ReservedConstraintType reservedConstraints;
        PathInfo pathInfo;
        CtrlPlanePathContent path;
        Layer2Info layer2Info;
        VlanTag srcVtag;
        String srcVlan = "null";
        Boolean isSrcTagged = false;
        VlanTag destVtag;
        String destVlan = "null";
        Boolean isDstTagged = false;
        
        List<OSCARSFaultReport> oscarsFaults;
        
        try
        {
	        response = oscarsClient.queryReservation(query);		// Submit queryReservation request to OSCARS and get response back
	        
	        oscarsFaults = response.getErrorReport();
	        
	        if(oscarsFaults != null)
	        {
	        	for(OSCARSFaultReport oneReport : oscarsFaults)
	        	{
	        		String faultyReply = oneReport.getErrorCode() + "\n" + oneReport.getErrorMsg() + "\nError Type: " + oneReport.getErrorType();
	        			        		
	        		reply.add(0, "Error: Exception (" + faultyReply + ")");
	        		
	        		return reply;
	        	}
	        }
	        
	        responseDetails = response.getReservationDetails();
	        reservedConstraints = responseDetails.getReservedConstraint(); 
	        
	        if(reservedConstraints == null)
	        {
	        	reply.add(0, "Error: No reservation matches that GRI");
	        	
	        	return reply;
	        }
	        
	        pathInfo = reservedConstraints.getPathInfo();
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
	        reply.add(String.valueOf(reservedConstraints.getStartTime()));
	        reply.add(String.valueOf(reservedConstraints.getEndTime()));
	        reply.add(String.valueOf(reservedConstraints.getBandwidth()));
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
    public ArrayList<String> cancelReservation(String gri) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5    
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
    public ArrayList<String> listReservations(String grisString) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5  
    	String[] gris = grisString.split(";");     
    	
    	
    	ArrayList<String> oneQueryResponse;
        ArrayList<String> reply = new ArrayList<String>();
        

        for (int i = 0; i < gris.length; i++) 
        {
        	
        	System.out.println("Querying GRI: " + gris[i]);
        	
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
    public ArrayList<String> modifyReservation(String gri, long startTimestamp, long endTimestamp) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5  
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
    public ArrayList<String> createPath(String gri) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5          
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
    public ArrayList<String> teardownPath(String gri) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5 
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
    public ArrayList<String> listAllReservations(String status) 
    {
    	//String repo = repoDir;							// NOT ENTIRELY SURE WHAT THIS VARIABLE IS FOR -- WAS USED IN SETUP IN v0.5 
                
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
    
    
    //public ArrayList<String> getTopology(String oscars_url) {}				-----> NO LONGER SUPPORTED BY API in v0.6 -- NEEDS TO BE IMPLEMENTED FOR MEICAN TO OPERATE PROPERLY!!!!
    //public ArrayList<String> refreshPath(String oscars_url, String gri){}		-----> NO LONGER SUPPORTED BY API in v0.6
    
}
