package ackwire;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.JSONObject;

public class TestClient{
    Socket requestSocket;
	 BufferedReader in;
	 PrintWriter out;
	Scanner scan = new Scanner(System.in);

    JSONObject message = new JSONObject();
    TestClient(){}
    void run()
    {
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 8484);
            System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
    	    in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
    	    out = new PrintWriter(requestSocket.getOutputStream(), true);
    	    while (requestSocket.isBound())
    	    {
    	      	try {
    	      		System.out.println("enter message");
    	      		String s = scan.nextLine();
    	      		sendMessage(s);
    	      	}
    	    	finally{System.out.println("----------");}
    	}
        }
            //3: Communicating with the server
           
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        
    }
    private void sendMessage(String string) {
    	message.put("client", string);
    	out.println(message.toString());
    	out.flush();
    	System.out.println("SENT");
    	}
    public static void main(String args[])
    {
        TestClient client = new TestClient();
        client.run();
    }
}
