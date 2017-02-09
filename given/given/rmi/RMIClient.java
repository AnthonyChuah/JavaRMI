/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import common.MessageInfo;

public class RMIClient {
    public static void main(String[] args) {
	if (args.length < 2){
	    System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
	    System.exit(-1);
	}
	System.out.println("Launched RMI Client.");
	String urlServer = new String("rmi://" + args[0] + "/RMIServer");
	int numMessages = Integer.parseInt(args[1]);
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	try {
	    String name = "RMIServer";
	    Registry registry = LocateRegistry.getRegistry(args[0]);
	    RMIServerI iRMIServer = (RMIServerI) registry.lookup(name);
	    System.out.println("Sending " + numMessages + " messages to server!");
	    for (int i = 0; i < numMessages; ++i) {
		iRMIServer.receiveMessage(new MessageInfo(numMessages, i));
	    }
	} catch (Exception e) {
	    System.out.println("RMIClient exception: ");
	    e.printStackTrace();
	}
    }
}
