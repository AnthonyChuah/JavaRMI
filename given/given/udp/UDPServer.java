/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

    private DatagramSocket recvSoc;
    private int totalMessages = -1;
    private int[] receivedMessages;
    private boolean close;

    private void run() {
	int				pacSize;
	byte[]			pacData;
	DatagramPacket 	pac;
	pacSize = 256;
	pacData = new byte[pacSize];
	// TO-DO: Receive the messages and process them by calling processMessage(...).
	//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
	pac = new DatagramPacket(pacData, pacData.length);
	int timeOut = 0;
	try {
	    System.out.println("UDPServer running.");
	    recvSoc.setSoTimeout(10000);
	    while (true) {
		try {
		    recvSoc.receive(pac);
		    pacData = pac.getData();
		    String msg = new String(pacData, "UTF-8");
		    processMessage(msg);
		} catch (SocketTimeoutException e) {
		    System.out.println("Socket timed out. Missing messages were: ");
		    for (int i = 0; i < totalMessages; ++i) {
			if (receivedMessages[i] == 0) {
			    System.out.print(i + " ");
			}
		    }
		    System.out.println("Timeout! " + e);
		    recvSoc.close();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void processMessage(String data) {
	MessageInfo msg = null;
	// System.out.println("Received message that looks like this: " + data);
	// System.out.println("Trimming trailing blanks.");
	data = data.trim(); // You need to trim trailing blanks for MessageInfo to parse.
	// TO-DO: Use the data to construct a new MessageInfo object
	try {
	    msg = new MessageInfo(data);
	    int msgNum = msg.messageNum;
	    int total = msg.totalMessages;
	    // TO-DO: On receipt of first message, initialise the receive buffer
	    if (totalMessages < 0) {
		++totalMessages;
		receivedMessages = new int[total];
	    }
	    // TO-DO: Log receipt of the message
	    receivedMessages[msgNum] = 1;
	    System.out.println("Received message " + msgNum + " of total " + total);
	    ++totalMessages;
	    System.out.println("TotalMessages incremented to: " + totalMessages);
	    // TO-DO: If this is the last expected message, then identify
	    //        any missing messages
	    if (msgNum == total - 1) {
		System.out.println("Final message received. Missing messages were: ");
		for (int i = 0; i < total; ++i) {
		    if (receivedMessages[i] == 0) {
			System.out.print(i + " ");
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public UDPServer(int rp) {
	// TO-DO: Initialise UDP socket for receiving data
	try {
	    this.recvSoc = new DatagramSocket(rp);
	} catch (SocketException e) {
	    e.printStackTrace();
	}
	this.close = false;
	// Done Initialisation
	System.out.println("UDPServer ready");
    }

    public static void main(String args[]) {
	int	recvPort;
	// Get the parameters from command line
	if (args.length < 1) {
	    System.err.println("Arguments required: recv port");
	    System.exit(-1);
	}
	recvPort = Integer.parseInt(args[0]);
	// TO-DO: Construct Server object and start it by calling run().
	try {
	    UDPServer server = new UDPServer(recvPort);
	    server.run();
	} catch (Exception e) {
	    System.out.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }

}
