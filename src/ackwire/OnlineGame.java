package ackwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;


public class OnlineGame {

	
	 ServerSocket localSocket;
	 Socket userConnection = null;
	 BufferedReader in;
	 PrintWriter out;
	 JSONObject message = new JSONObject();
	 String s;
	 
public void run(){
	try{
        // Set up the ServerSocket
        localSocket = new ServerSocket(8484);
        
        // Listen for a connection
        System.out.println("Waiting for client");
        userConnection = localSocket.accept();
        
        System.out.println("Connection received from " + userConnection.getInetAddress().getHostName());
        
        //  Setup ObjectInput and ObjectOutput streams
	    in = new BufferedReader(new InputStreamReader(userConnection.getInputStream()));
	    out = new PrintWriter(userConnection.getOutputStream(), true);
        sendMessage("Connection successful");
        while (localSocket.isBound())
	    {
	      	try {
	      		String s = in.readLine();
	      		System.out.println(s);
	      		
	      	}
	    	finally{System.out.println("----------");}
	}}
        // Game communication occurs here after connection is established. 
        
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    
}

private void sendMessage(String string) {
	message.put("Server", string);
	out.write(message.toString());
	out.flush();
	System.out.println("server>" + message.toString());
	}
public static void main(String args[]){
    OnlineGame ackwire = new OnlineGame();
   
        ackwire.run();
//	String s = "{\"hi\":\"there\"}";
//	JSONObject test = new JSONObject(s);
//	test.put("hi", "there");
//	System.out.println(test);
   
}
}