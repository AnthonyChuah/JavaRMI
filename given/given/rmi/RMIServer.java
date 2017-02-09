/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

	    // TO-DO: On receipt of first message, initialise the receive buffer
	    if (totalMessages < 0) {
		receivedMessages = new int[msg.totalMessages];
		totalMessages = 0;
	    }

	    // TO-DO: Log receipt of the message
	    System.out.println("Received message number " + msg.messageNum);
	    receivedMessages[msg.messageNum] = 1;
	    ++totalMessages;

	    // TO-DO: If this is the last expected message, then identify
	    //        any missing messages
	    if (totalMessages == msg.totalMessages) {
		System.out.println("Missing messages:");
		for (int i = 0; i < totalMessages; ++i) {
		    if (receivedMessages[i] == 0) {
			System.out.print(msg.messageNum + " ");
		    }
		}
	    }

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}

		try {
		    // TO-DO: Instantiate the server class
		    // System.setProperty("java.rmi.server.hostname","192.168.56.1");
		    String name = "RMIServer";
		    RMIServer myServer = new RMIServer();
		    // RMIServerI stub = (RMIServerI) UnicastRemoteObject.exportObject(myServer, 0);
		    // 0 second arg means anonymous port.
		    // TO-DO: Bind to RMI registry
		    Registry registry = LocateRegistry.getRegistry(1099);
		    registry.rebind(name, myServer);
		    System.out.println("Server ready");
		} catch (Exception e) {
		    System.out.println("Server exception: " + e.toString());
		    e.printStackTrace();
		}

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
	    try {
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry registry = LocateRegistry.getRegistry();

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		registry.rebind(serverURL, server);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
}
