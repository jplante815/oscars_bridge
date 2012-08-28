package net.es.oscars.meican.common;

import javax.xml.ws.Endpoint;

import net.es.oscars.meican.server.OSCARSBridgeWebService;

/**
* @author Jeremy
*
* Main class used to deploy the OSCARSBridgeV6 WebService. Run this and you don't have to do anything else fancy to deploy the service.
**/
public class Invoker 
{
	private static String deployTarget = "http://localhost:8082/OSCARSBridgeV6";		// Where do you want the service to be deployed?
	
	public static void main(String[] args) 
	{
		OSCARSBridgeWebService bridge = new OSCARSBridgeWebService();

		Endpoint.publish(deployTarget, bridge);		// Deploy it at specified target location
		
		System.out.println("\n\n-----\nDeploying OSCARSBridgeV6 WebService at " + deployTarget);
		System.out.println("In your browser, enter URL: " + deployTarget + "?wsdl to verify the service has been deployed.\n-----\n");
	}
}
