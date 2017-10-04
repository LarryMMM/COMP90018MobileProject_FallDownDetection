package Client;
/*The EZshare client package implements an application that
* client end can use command line to communicate with server.
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.*;
import org.json.JSONObject;

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.EOFException;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public class Client {
	// Define constant LOGGER,TIMEOUT
	public static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	public static final int TIMEOUT = 10000;
	static String ip = "localhost";
	static int port = 3000;
	static CommandLine cmd;
    static long startime = System.currentTimeMillis();
	public static void main(String[] args) throws org.apache.commons.cli.MissingArgumentException {

		cmd = commandOptions.getOptions(args);
		if (cmd == null) {
			System.out.println("Please check your input");
			System.exit(0);
		}
		if (cmd.hasOption("debug")) {
			LOGGER.log(Level.INFO, "setting debug on");
			LOGGER.setLevel(Level.FINE);
			ConsoleHandler handler = new ConsoleHandler();
			handler.setLevel(Level.FINE);
			LOGGER.addHandler(handler);
		}
		getHostPort(cmd);
		Socket socket;
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		boolean secureLink=false;//need to emerge with security conditions
		if(secureLink){
			try{
				System.setProperty("javax.net.ssl.trustStore", "clientKeyStore/myGreatName");//needs to change
				//Create SSL socket and connect it to the remote server 
				SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(ip, port);			
				DataOutputStream output = new DataOutputStream(sslsocket.getOutputStream());
				output.writeUTF(commandLine.checkOptions(cmd).toString());
				output.flush();
				DataInputStream input = new DataInputStream(sslsocket.getInputStream());
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				if(cmd.hasOption("subscribe")){
					Thread t=new Thread(){
						public void run(){
							boolean enterFlag=true;
							while(enterFlag){
								try{
									if(System.in.read()=='\n'){
										String id=cmd.getOptionValue("id");	
										output.writeUTF(toJSON.unsubscribe(id).toString());//send unsubscribe
										enterFlag=false;
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}

						}
					};
					t.start();
				}
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				while(true){
					if(!cmd.hasOption("subscribe")){//time out only when no subscribe
						if(System.currentTimeMillis()-startime>=TIMEOUT){
							System.out.println("Time out reached, disconnected");
							break;
						}
					}
					try{
						JSONObject message = new JSONObject(input.readUTF());
						System.out.println(message.toString());//encypted msg //need test
						downloadFile.run(message.toString(), input);
						if(message.has("resultSize")){break;}//terminate after receive resultsize
					}catch(EOFException e){
						System.out.println("connection lost");
						break;//connection lost
					}
					catch(Exception e){
						e.printStackTrace();
					}

				}
				
			}catch (Exception exception) {
				exception.printStackTrace();
			}
			
		}else{
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			try {
	           	// send output command to server
				String command = commandLine.checkOptions(cmd).getString("command");
				socket = connectServer(ip, port);
				LOGGER.log(Level.FINE, command +  " to "+ ip+":" + port);
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				output.writeUTF(commandLine.checkOptions(cmd).toString());
				output.flush();
				System.out.println(commandLine.checkOptions(cmd).toString());
				LOGGER.fine("SENT: " + commandLine.checkOptions(cmd));
				
				// print out input
				DataInputStream input = new DataInputStream(socket.getInputStream());
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				if(cmd.hasOption("subscribe")){
					Thread t=new Thread(){
						public void run(){
							boolean enterFlag=true;
							while(enterFlag){
								try{
									if(System.in.read()=='\n'){
										String id=cmd.getOptionValue("id");	
										output.writeUTF(toJSON.unsubscribe(id).toString());//send unsubscribe
										enterFlag=false;
									}
								}catch(Exception e){
									e.printStackTrace();
								}
							}

						}
					};
					t.start();
				}
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
				while (true) {					
					if(!cmd.hasOption("subscribe")){//time out only when no subscribe
						if(System.currentTimeMillis()-startime>=TIMEOUT){
							System.out.println("Time out reached, disconnected");
							break;
						}
					}
					if(input.available()>0){
						//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
						JSONObject message =new JSONObject(input.readUTF());
						//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
						System.out.println(message);
						LOGGER.log(Level.FINE, "RECEIVED: " + message);
						// if there is a file, download input file
						//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
						downloadFile.run(message.toString(), input);						
						if(message.has("resultSize")){break;}//terminate after receive resultsize
						//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
	              	}				  
				}	
			} catch (UnknownHostException e) {
			} catch (IOException e) {
				System.out.println("Connection failed, please check with the server");
			} catch (org.json.JSONException e){
		    }
		}
	}

	// connect to server
	public static Socket connectServer(String host, int port) throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		socket.setSoTimeout(TIMEOUT);
		LOGGER.log(Level.FINE, "Connection Established to " + host + ":" + port);
		return socket;
	}
    
	
    // get host and port number from command line input
	public static void getHostPort(CommandLine cmd) {
		if (cmd.hasOption("host")) {
			try {
				ip = cmd.getOptionValue("host");
				port = Integer.parseInt(cmd.getOptionValue("port"));
			} catch (NumberFormatException e1) {
				System.out.println("Please enter correct host and port number");
			}
		}
	}

}
