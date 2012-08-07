package bridge;

import javax.xml.ws.Endpoint;

/**
* @author Jeremy
*
* Main class used to deploy the OSCARSBridgeV6 WebService. Run this and you don't have to do anything else fancy to deploy the service.
**/
public class WSPublisher 
{
	private static String deployTarget = "http://localhost:8080/OSCARSBridgeV6";		// Where do you want the service to be deployed?
	
	public static void main(String[] args) 
	{
		Endpoint.publish(deployTarget, new OSCARSBridge());		// Deploy it at specified target location
		
		System.out.println("Deploying OSCARSBridgeV6 WebService at " + deployTarget);
		System.out.println("In your browser, enter URL: " + deployTarget + "?wsdl to verify the service has been deployed.");
	}
}
