/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
	    if (totalMessages < 0) {
		receivedMessages = new int[msg.totalMessages];
		totalMessages = 0;
	    }
	    System.out.println("Received message number " + msg.messageNum);
	    receivedMessages[msg.messageNum] = 1;
	    ++totalMessages;
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
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new RMISecurityManager());
		}
		try {
		    String name = "RMIServer";
		    RMIServer myServer = new RMIServer();
		    Registry registry = LocateRegistry.createRegistry(1099);
		    registry.rebind(name, myServer);
		    System.out.println("Server ready");
		} catch (Exception e) {
		    System.out.println("Server exception: " + e.toString());
		    e.printStackTrace();
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
	    try {
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(serverURL, server);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
}
