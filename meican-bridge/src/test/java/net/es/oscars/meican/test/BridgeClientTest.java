package net.es.oscars.meican.test;

import java.util.ArrayList;

import javax.xml.ws.Endpoint;

import net.es.oscars.meican.server.OSCARSBridgeWebService;
import org.testng.annotations.Test;

@Test
public class BridgeClientTest 
{

	@Test
	public void testBridge() throws InterruptedException
	{
		String deployTarget = "http://localhost:8082/OSCARSBridgeV6";		// Where the Bridge will be deployed. MEICAN will need to know this location
		
        String oscarsUrl = "http://localhost:9001/OSCARS";   				// Where to find the local instance of OSCARS
        String topoBridge_url = "http://localhost:9019/topoBridge";			// Where to find the local instance of Topology bridge

		OSCARSBridgeWebService bridgews = new OSCARSBridgeWebService();
		Endpoint.publish(deployTarget, bridgews);							// Deploy Bridge WS at specified target location
			
		System.out.println("\n-- Testing Connection to OSCARS -- ");
		bridgews.buildBridge(oscarsUrl);
		System.out.println("-- Test Complete -- \n");

		
		System.out.println("-- Testing Connection to TopoBridge -- ");
		ArrayList<String> theTopology = bridgews.getTopology(topoBridge_url, "testdomain-1");
		if(theTopology.get(0).equals(""))		// No problems connecting to TopoBridge
		{
			System.out.println("Domain ID: " + theTopology.get(1));
			System.out.println("URNs:");
			for (String t : theTopology)
			{
				if(t.contains("link="))		// Just print the URNs for test purposes
					System.out.println(t.substring(0, t.indexOf(" ")));
			}
		}
		System.out.println("-- Test Complete -- \n");
		
		/*
        ArrayList<String> resultArray = new ArrayList<String>();
        long myTime = (long)(System.currentTimeMillis() / 1000L);

               
        /*
		// TEST createReservation() //
        System.out.println("Creating new Reservation");
		resultArray = (ArrayList<String>)bridgews.createReservation("Reservation from BRIDGEv0.6", "urn:ogf:network:domain=testdomain-1:node=node-1-1:port=ge-1/1/0:link=*", "true", "any", "urn:ogf:network:domain=testdomain-1:node=node-1-2:port=ge-1/1/0:link=*", "true", "any", "null", 25, "timer-automatic", myTime+3600, myTime+3840);
		System.out.println(resultArray.toString());
		System.out.println("Creation done!");
		System.out.println("==============");
	
		/*
		// TEST queryReservation() //
		System.out.println("Querying Reservation");
		String griToQuery = resultArray.get(1);
		Thread.sleep(10000);
		resultArray = (ArrayList<String>)bridge.queryReservation(griToQuery);
		System.out.println(resultArray.toString());
		System.out.println("Query successful!");
		System.out.println("==============");
		
		        
		/*
		// TEST cancelReservation() //
		System.out.println("Connection successful, Cancelling");
		String griToCancel = griToQuery;
		resultArray = (ArrayList<String>)bridge.cancelReservation(griToCancel);
		System.out.println(resultArray.toString());
		System.out.println("Query successful!");
		System.out.println("==============");
		*/
		        
		/*
		// TEST listReservations() //
		System.out.println("Connection successful, Listing");
		String multiGRI = "testdomain-1-14;testdomain-1-15;testdomain-1-16;testdomain-1-17;testdomain-1-18";
		resultArray = test.listReservations(multiGRI);
		System.out.println(resultArray.toString());
		System.out.println("Listing done!");
		*/
		        
		/*
		// TEST modifyReservation() //
		System.out.println("Connection successful, Creating preliminary reservation");
		resultArray = bridge.createReservation("Reservation from BRIDGEv0.6", "urn:ogf:network:domain=testdomain-1:node=node-1-1:port=ge-1/1/0:link=*", "true", "any",
		"urn:ogf:network:domain=testdomain-1:node=node-1-2:port=ge-1/1/0:link=*", "true", "any", "null", 25, "timer-automatic", myTime+3600, myTime+3840);
		System.out.println(resultArray.toString());
		System.out.println("Creation done!");
		String griToModify = resultArray.get(1);
		Thread.sleep(10000);
		System.out.println("==============");
		System.out.println("Querying preliminary request");
		resultArray = bridge.queryReservation(griToModify);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		System.out.println("Modifying preliminary request");
		resultArray = bridge.modifyReservation(griToModify, myTime+3700, myTime+3820);
		System.out.println(resultArray.toString());
		System.out.println("Modification done!");
		Thread.sleep(10000);
		System.out.println("==============");
		System.out.println("Querying modified request");
		resultArray = bridge.queryReservation(griToModify);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		*/
		        
		/*
		// TEST createPath() & teardownPath() //
		System.out.println("Connection successful, Creating preliminary reservation");
		resultArray = test.createReservation("Reservation from BRIDGEv0.6", "urn:ogf:network:domain=testdomain-1:node=node-1-1:port=ge-1/1/0:link=*", "true", "any",
		"urn:ogf:network:domain=testdomain-1:node=node-1-2:port=ge-1/1/0:link=*", "true", "any", "null", 25, "signal-xml", myTime+5, myTime+125);
		System.out.println(resultArray.toString());
		System.out.println("Reservation Creation done!");
		String griToCreatePathFor = resultArray.get(1);
		Thread.sleep(10000);
		System.out.println("==============");
		System.out.println("Querying preliminary reservation"); //Query the gri: RESERVED
		resultArray = test.queryReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		System.out.println("Creating path for reservation"); //Create Path: INSETUP
		resultArray = test.createPath(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Path Creation done!");
		Thread.sleep(5000);
		System.out.println("==============");
		System.out.println("Querying reservation"); //Query the gri: ACTIVE
		resultArray = test.queryReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		System.out.println("Tearing Down path for reservation"); //Teardown Path: INTEARDOWN
		resultArray = test.teardownPath(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Path Teardown done!");
		Thread.sleep(5000);
		System.out.println("==============");
		System.out.println("Querying reservation"); //Query the gri: RESERVED
		resultArray = test.queryReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		System.out.println("Creating path for reservation"); //Create Path again: INSETUP
		resultArray = test.createPath(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Path Creation done!");
		Thread.sleep(5000);
		System.out.println("==============");
		System.out.println("Querying reservation"); //Query the gri: ACTIVE
		resultArray = test.queryReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		System.out.println("Cancelling Reservation"); //Cancel the gri: INCANCEL
		resultArray = test.cancelReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Cancellation done!");
		Thread.sleep(5000);
		System.out.println("==============");
		System.out.println("Querying cancelled reservation"); //Query the gri: CANCELLED
		resultArray = test.queryReservation(griToCreatePathFor);
		System.out.println(resultArray.toString());
		System.out.println("Query done!");
		System.out.println("==============");
		*/
		        
        /*
		// TEST listAllReservations() //
		System.out.println("Connection successful.");
		ArrayList<String> variousStatuses = new ArrayList<String>();
		variousStatuses.add("ACTIVE");
		variousStatuses.add("RESERVED");
		variousStatuses.add("FINISHED");
		variousStatuses.add("CANCELLED");
		variousStatuses.add("FAILED");
        
        for(String status : variousStatuses)
        {
         System.out.println("Now listing ALL reservations with status: " + status);
        
         resultArray = test.listAllReservations(status);
         System.out.println(resultArray.toString());
        
         System.out.println("Listing done!");
            System.out.println("==============");
        }
        
        System.out.println("\n* TEST listAllReservations() done.");
        */
	}
}