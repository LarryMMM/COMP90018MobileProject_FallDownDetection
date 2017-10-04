package Server;
/*The EZshare server package implements an application that
* .
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import java.io.DataInputStream;
import java.util.logging.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.net.ServerSocketFactory;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;

import Resource.Resource;

public class Server {
	public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	static String hostname = "Dl001";
	static int connectionLimit = 10;
	static int exchangeLimit = 600;
	static int port = 3000;
	static String secret;
	static ArrayList<JSONObject> currentServerList = new ArrayList<JSONObject>();
	static ArrayList<String> keyList =new ArrayList<String>();
	// Declare the port number
	// Identifies the user number connected
	private static int counter = 0;
	static HashMap<ArrayList<String>,Resource> hm = new HashMap<ArrayList<String>, Resource>();//action to resource
	static HashMap<ArrayList<String>,Resource> hm1 = new HashMap<ArrayList<String>, Resource>();//check existence of resource.
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	//static HashMap<ArrayList<String>,Resource> hm2=new HashMap<>;//record subscribe
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	static ArrayList<String> clientList = new ArrayList<String>();
	
	public static void main(String[] args) {
		Server.secret = randomString(40);

		CommandLine cmd = Functions.getOptions(args);
		
		if(cmd != null){
			if (cmd.hasOption("debug")) {
				LOGGER.log(Level.INFO, "setting debug on");
				LOGGER.setLevel(Level.FINE);
				ConsoleHandler handler = new ConsoleHandler();
				handler.setLevel(Level.FINE);
				LOGGER.addHandler(handler);
			}
			if(cmd.hasOption("advertisedhostname") ){
				hostname = cmd.getOptionValue("advertisedhostname");				
			}
			if(cmd.hasOption("connectionintervallimit") ){
				if(isInteger(cmd.getOptionValue("connectionintervallimit"))){
					connectionLimit = Integer.parseInt(cmd.getOptionValue("connectionintervallimit"));
				}else{System.out.println("invalid connection limit");}
			}
			if(cmd.hasOption("exchangeinterval") ){
				if(isInteger(cmd.getOptionValue("exchangeinterval"))){
					exchangeLimit = Integer.parseInt(cmd.getOptionValue("exchangeinterval"));
				}else{System.out.println("invalid exchange interval");}
			}
			if(cmd.hasOption("port") ){
				if(isInteger(cmd.getOptionValue("port")) ){
					port = Integer.parseInt(cmd.getOptionValue("port"));
				}else{System.out.println("invalid port");}
			}
			if(cmd.hasOption("secret") ){
				secret = cmd.getOptionValue("secret");		
			}						
			if(cmd.hasOption("")){
				System.out.println("Invalid input");
			}
		}else{
			System.out.println("invalid command line please check your input");
		}
		
		//System.out.println(hostname +" "+connectionLimit+" "+exchangeLimit+" "+secret+" "+port);
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		boolean secureLink=false;//need to emerge with security conditions
		if(secureLink){	
			System.setProperty("javax.net.ssl.keyStore","serverKeystore/aGreatName");
			//Password to access the private key from the keystore file
			System.setProperty("javax.net.ssl.keyStorePassword","comp90015");
			// Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
			System.setProperty("javax.net.debug","all");
			//above need to change with security
			try{
				SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
						.getDefault();
				SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);			
				//Accept client connection
				SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();	
				//Create buffered reader to read input from the client
				InputStream inputstream = sslsocket.getInputStream();
				InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
				BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
				System.out.println("Waiting for client connection..");
				System.out.println(secret);
				if (!currentServerList.isEmpty()){
					currentServerList.clear();;
				}
/*				Timer t1 = new Timer();
				t1.schedule(new TimerTask() {//set up a schedule for exchange serverlist
				    @Override
				    public void run() {
				    	Operations.sendExchange();//exchange serverlist
				    }
				}, 0, exchangeLimit*1000);	*/	
				while(true){
					
				}
				
			}catch (Exception exception) {
				exception.printStackTrace();
			}
		}else{
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			ServerSocketFactory factory = ServerSocketFactory.getDefault();
			try(ServerSocket server = factory.createServerSocket(port)){// Wait for connections.
				System.out.println("Waiting for client connection..");
				System.out.println(secret);
				if (!currentServerList.isEmpty()){
					currentServerList.clear();;
				}			
				
				Timer t1 = new Timer();
				t1.schedule(new TimerTask() {//set up a schedule for exchange serverlist
				    @Override
				    public void run() {
				    	Operations.sendExchange();//exchange serverlist
				    }
				}, 0, exchangeLimit*1000);
				
				while(true){
					//serverInteractions
					Socket client = server.accept();
					if (clientList.contains(client.getInetAddress().toString())){
						client.close();
				    	System.out.println("refused! too frequent connection");
					}else{
						clientList.add(client.getInetAddress().toString());
					}
					counter++;
					System.out.println("Client "+counter+": Applying for connection!");
					// Start a new thread for a connection
					Thread t = new Thread(() -> {
						try {
							serveClient(client);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					});
					t.start();
				}
			} catch (IOException e) {
	
			}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}
	
	private static void serveClient(Socket client) throws URISyntaxException {
		long startime = System.currentTimeMillis();
		try(Socket clientSocket = client){		
			clientSocket.setSoTimeout(60);
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.
					getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.
		    		getOutputStream());
		    //System.out.println("CLIENT: "+input.readUTF());	    
		    // Receive more data..
		    while(true){
/*		    	if(System.currentTimeMillis()-startime>=connectionLimit*1000){
					System.out.println("Time out reached, disconnected");
					break;
				}*/
		    	if(input.available() > 0){
		    		// Attempt to convert read data to JSON
		    		JSONObject command =new JSONObject (input.readUTF());
		    		LOGGER.log(Level.FINE, "RECEIVED: " + command.toString());
		    		if (Operations.genericCheck(command)==0){	
		    			String response = Operations.toResponse(command,output);
		    			output.writeUTF(response);
		    			LOGGER.log(Level.FINE, "SENT: " + response.toString());	
		    		}else if(Operations.genericCheck(command)==1){
		    			output.writeUTF(Operations.genericResponse(1).toString());
		    			LOGGER.log(Level.FINE, "SENT: " + Operations.genericResponse(1).toString());	
		    		}else{
		    			output.writeUTF(Operations.genericResponse(2).toString());
		    			LOGGER.log(Level.FINE, "SENT: " + Operations.genericResponse(2).toString());
		    		}
		    		try {
						Thread.sleep(connectionLimit*1000);
						clientList.remove(clientSocket.getInetAddress().toString());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		} catch (IOException e) {

		}
	}
	
	public static String randomString( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt(rnd.nextInt(AB.length()) ) );
		   return sb.toString();
	}
	public static boolean isInteger(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
		
	}
}
