package Server;
/*The EZshare server package implements an application that
* .
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Resource.Resource;
import Server.Server;
public class Operations {
	public static int genericCheck(JSONObject command){
		try{
			String strCom=command.getString("command");
			if(strCom.equals("PUBLISH")||strCom.equals("REMOVE")||strCom.equals("SHARE")
				||strCom.equals("QUERY")||strCom.equals("FETCH")||strCom.equals("EXCHANGE")	){
				return 0;
			}else if(strCom.isEmpty()){return 1;}
			else{
				return 2;
			}
		}catch(JSONException e){
			return 1;//no command or incorrect type
		}
	}
	
	
	public static JSONObject genericResponse(int condition) {
		JSONObject response=new JSONObject();
		if (condition == 1){
			response.put("response", "error");
			response.put("errorMessage", "missing or incorrect type for command");
			return response;
		}else if (condition == 2){
			response.put("response", "error");
			response.put("errorMessage", "invalid command");
			return response;
		}else{//in case to add other possible responses
			response.put("response", "test");
			response.put("errorMessage", "invalid test");
			return response;
		}
	}
	
	
	public static void sendExchange() {
		if(!Server.currentServerList.isEmpty()){
			Random r =new Random();
			int randomNum = r.nextInt(Server.currentServerList.size());
			JSONObject server=new JSONObject();
			server=	Server.currentServerList.get(randomNum);
			String hostname=server.getString("hostname");
			int port=server.getInt("port");
			try(Socket socket = new Socket(hostname, port)){
				// Output and Input Stream
				DataInputStream input = new DataInputStream(socket.
						getInputStream());
			    DataOutputStream output = new DataOutputStream(socket.
			    		getOutputStream());
			    
			    JSONObject exchangeList=new JSONObject();
			    exchangeList.put("command", "EXCHANGE");
			    //JSONObject[] serverList=new JSONObject[Server.currentServerList.size()];
			    for(int i=0;i<Server.currentServerList.size();i++){
			    	exchangeList.append("serverList", Server.currentServerList.get(i));
			    }			    
			    output.writeUTF(exchangeList.toString()); 
			}catch (UnknownHostException e) {
				Server.currentServerList.remove(randomNum);			
			}catch (IOException e) {
				Server.currentServerList.remove(randomNum);
			}
		}
	}
	
	public static String toResponse(JSONObject command,DataOutputStream fileOutput) throws URISyntaxException, JSONException, IOException{
		JSONObject response=new JSONObject();
		String strCom=command.getString("command");
		if(strCom.equals("PUBLISH")){
			if(resourceCheck(command)==0){
				Functions.publishResource(command);
				response.put("response", "success");
				return response.toString();
			}else if(resourceCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resource");
				System.out.println("sysout1");
				return response.toString();
			}else if(resourceCheck(command)==2){
				response.put("response", "error");
				response.put("errorMessage", "invalid resource");
				return response.toString();
			}else{
				response.put("response", "error");
				response.put("errorMessage", "cannot publish resource");
				return response.toString();
			}
		}
		else if(strCom.equals("REMOVE")){
			if(resourceCheck(command)==0){
				return Functions.removeResource(command);
			}else if(resourceCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resource");
				return response.toString();
			}else if(resourceCheck(command)==2){
				response.put("response", "error");
				response.put("errorMessage", "invalid resource");
				return response.toString();
			}else{
				response.put("response", "error");
				response.put("errorMessage", "cannot remove resource");
				return response.toString();
			}
		}
		else if(strCom.equals("SHARE")){
			if(resourceCheck(command)==0){
				if (secretCheck(command)==0){
					Functions.shareResource(command);
					response.put("response", "success");
					return response.toString();
				}else if (secretCheck(command)==1){
					response.put("response", "error");
					response.put("errorMessage", "missing secret");
					return response.toString();
				}else{
					response.put("response", "error");
					response.put("errorMessage", "incorrect secret");
					return response.toString();
				}
			}else if(resourceCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resource");
				return response.toString();
			}else if(resourceCheck(command)==2){
				response.put("response", "error");
				response.put("errorMessage", "invalid resource");
				return response.toString();
			}else{
				response.put("response", "error");
				response.put("errorMessage", "cannot share resource");
				return response.toString();
			}
		}
		else if(strCom.equals("QUERY")){
			//if(queryCheck(command)==0){
			if(resourceTemplateCheck(command)==0){
				response.put("response", "success");
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>				
				return response.toString()+resultGather(command);			
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
			}//else if(queryCheck(command)==1){
			else if(resourceTemplateCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resourceTemplate");
				return response.toString();
			}//else if(queryCheck(command)==3){
			else if(resourceTemplateCheck(command)==3){
				response.put("response", "error");
				response.put("errorMessage", "incorrect query command");
				return response.toString();
				}
			else{
				response.put("response", "error");
				response.put("errorMessage", "invalid resourceTemplate");
				return response.toString();
			}
		}
		else if(strCom.equals("FETCH")){
			if(resourceTemplateCheck(command)==0){
				return Functions.fetchResource(command,fileOutput);
			}else if(resourceTemplateCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resourceTemplate");
				return response.toString();
			}else{
				response.put("response", "error");
				response.put("errorMessage", "invalid resourceTemplate");
				return response.toString();
			}
		}
		else/* if(strCom.equals("EXCHANGE"))*/{
			if (serverListCheck(command)==0){
				Functions.exchangeServerlist(command);
				response.put("response", "success");
				return response.toString();
			}else if (serverListCheck(command)==1){
				response.put("response", "error");
				response.put("errorMessage", "missing resourceTemplate");
				return response.toString();
			}else{
				response.put("response", "error");
				response.put("errorMessage", "missing or invalid server list");
				return response.toString();
			}
		}
	}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>		
	private static String resultGather(JSONObject command) throws JSONException, URISyntaxException {		
		String str="";
		int j=0;
		if(command.getBoolean("relay")==true){
			JSONObject intervalCommand=command;
			intervalCommand.put("relay", false);
			for(JSONObject serverlist :Server.currentServerList){
				//send query command to other server
				System.out.println("checking all serverlist");
				try(Socket socket = new Socket(serverlist.getString("hostname"), serverlist.getInt("port"))){
					//connect to other server
					DataInputStream input = new DataInputStream(socket.getInputStream());
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());
					output.writeUTF(intervalCommand.toString());
					output.flush();
					while (true) {								
						if (input.available() > 0) {	
							JSONObject result =new JSONObject(input.readUTF());//result from other server
							try{										
								if(result.has("resultSize")){
									j+=result.getInt("resultSize");
									break;
								}else if(result.has("response")){
									continue;
								}
								else{
									str+=result.toString()+"\n";
								}
							}catch(Exception e){e.printStackTrace();}								
						}							
					}		
				}catch (UnknownHostException e) {
				}catch (IOException e) {
				}						
			}//all server are query once and str=all rss,j=resultSize
			ArrayList<Resource> rss=Functions.queryResource(command);//query localhost
			int jlocal=rss.size();
			for(int i=0;i<jlocal;i++){
				str+=rss.get(i).toJsonJson().toString()+"\n";
				j++;
			}
			JSONObject resultSize=new JSONObject();
			resultSize.put("resultSize", j);
			return "\n"+str+resultSize.toString();
		}
		else{
			ArrayList<Resource> rss=Functions.queryResource(command);//query localhost
			int jlocal=rss.size();
			for(int i=0;i<jlocal;i++){
				str+=rss.get(i).toJsonJson().toString()+"\n";
				j++;
			}
			JSONObject resultSize=new JSONObject();
			resultSize.put("resultSize", j);
			return "\n"+str+resultSize.toString();					
		}//relay field is false							
	}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	

	public static int queryCheck(JSONObject command) {
		try{
		    JSONObject resourceTemplate = command.getJSONObject("resourceTemplate");
			String channel=resourceTemplate.getString("channel");
			
			if (channel.contains("\0")||channel.contains(" ")){
				return 2;
			}
			String uriString=resourceTemplate.getString("uri");
			URI uri = new URI(resourceTemplate.getString("uri"));
			if (uriString.contains("\0")||uriString.contains(" ")){
				return 2;
			}
		}catch(JSONException e){
			System.out.println("1");
			return 1;
		}catch(URISyntaxException e){
			return 2;
		}
		try{boolean relay = command.getBoolean("relay");}catch(JSONException e){return 3;}
		
		return 0;//success
	}
	
	public static int serverListCheck(JSONObject command) {
		try{
			JSONArray serverList;
			serverList =command.getJSONArray("serverList");
				for (int i=0; i< serverList.length(); i++){
					JSONObject serverInfo =serverList.getJSONObject(i);
					String hostname=serverInfo.getString("hostname");
					int port=serverInfo.getInt("port");
					try{
						Socket serverCheck=new Socket(hostname,port);			
						if(serverCheck.isConnected()){
							System.out.println("not connected");
							return 0;
						}else{
							System.out.println("not connected");
							return 2;
						}
					}catch (UnknownHostException e) {
						return 2;
					}catch (IOException e1) {
						return 2;
					}
				}
		}catch(JSONException e){
			return 2;//missing
		}
		return 0;
	}
	
	public static int resourceTemplateCheck(JSONObject command) {	
		try{
			JSONObject resourceTemplate = command.getJSONObject("resourceTemplate");
			try{
				String name= resourceTemplate.getString("name");//check name field
				if (name.contains("\0")||name.startsWith(" ")||name.endsWith(" ")){
					return 2;		//all string cannot have ""		
				}
				JSONArray tags=resourceTemplate.getJSONArray("tags");//check tags field &&type
				for(Object tag: tags){
					if(tag.toString().contains("\0")||tag.toString().startsWith(" ")||tag.toString().endsWith(" ")){return 2;}//all string cannot have ""
				}			
				String description=resourceTemplate.getString("description");//check description field &&type
				if (description.contains("\0")||description.startsWith(" ")|| description.endsWith(" ")){
					return 2;
				}
				String uriString=resourceTemplate.getString("uri");
				if(uriString.contains(" ")){return 2;}
				URI uri=new URI(resourceTemplate.getString("uri"));
				if (command.getString("command").equals("FETCH") ){
				if (uriString.contains("\0")||uriString.startsWith(" ")||uriString.endsWith(" ")||uriString.equals(null)||uriString.isEmpty()||!uri.isAbsolute()){
					return 2;
				}
				}
				if (command.getString("command").equals("QUERY") ){
					if (uriString.isEmpty()){
						
					}else{
						if (uriString.contains("\0")||uriString.startsWith(" ")||uriString.endsWith(" ")||!uri.isAbsolute()){
						return 2;
						}
					}
				}
				
				if (command.getString("command").equals("FETCH") &&(!(uri.getScheme().equals("file"))||uriString.isEmpty())){
					return 2;
				}		
				if (command.getString("command").equals("QUERY") ){
					try{boolean relay = command.getBoolean("relay");}catch(JSONException e){return 3;}
				}
				String channel=resourceTemplate.getString("channel");
				if (channel.contains("\0")||channel.startsWith(" ")||channel.endsWith(" ")){
					return 2;
				}
				String owner = resourceTemplate.getString("owner");
				if (owner.contains("\0")||owner.startsWith(" ")||owner.endsWith(" ")){
					return 2;
				}
				if (owner.equals("*")){
					return 2;
				}
				String ezserver=resourceTemplate.getString("ezserver");
				if (ezserver.contains("\0")||ezserver.startsWith(" ")||ezserver.endsWith(" ")){
					return 2;
				}
				//all fields checked as valid
				String[] key={channel,uriString};
				ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
				if(Server.hm1.get(keyList)==null&&command.getString("command").equals("FETCH")){
					return 1;//
				}				
			}catch(JSONException e){return 2;}//invalid bc missing some fields in resource
			catch(URISyntaxException e){
				return 2;
			}
		}catch(JSONException e){
			return 1;
		}

		return 0;//success
	}
	
	private static int secretCheck(JSONObject command) throws URISyntaxException {		
		try{
			JSONObject resource = command.getJSONObject("resource");
			String password = command.getString("secret");
			System.out.println(password);
			System.out.println(Server .secret);
			if (password.equals(Server.secret)){
				return 0;
			}else{
				return 2;
			}
		}catch(JSONException e){
			System.out.println("1");
			return 1;
		}
	}
	
	public static int resourceCheck(JSONObject js){	
		try{
			JSONObject resource = js.getJSONObject("resource");//check resource field
			try{
				String name= resource.getString("name");//check name field
				if (name.contains("\0")||name.startsWith(" ")||name.endsWith(" ")){
					return 2;		//all string cannot have ""		
				}
				JSONArray tags=resource.getJSONArray("tags");//check tags field &&type
				for(Object tag: tags){
					if(tag.toString().contains("\0")||tag.toString().startsWith(" ")||tag.toString().endsWith(" ")){return 2;}//all string cannot have ""
				}			
				String description=resource.getString("description");//check description field &&type
				if (description.contains("\0")||description.startsWith(" ")|| description.endsWith(" ")){
					return 2;
				}
				String uriString=resource.getString("uri");
				if(uriString.contains(" ")){return 2;}
				URI uri=new URI(resource.getString("uri"));
				if (uriString.contains("\0")||uriString.startsWith(" ")||uriString.endsWith(" ")||uriString.equals(null)||uriString.isEmpty()||!uri.isAbsolute()){
					return 2;
				}
				if(js.getString("command").equals("SHARE")&&(!(uri.getScheme().equals("file"))||uriString.isEmpty())){return 2;}
				String channel=resource.getString("channel");
				if (channel.contains("\0")||channel.startsWith(" ")||channel.endsWith(" ")){
					return 2;
				}
				String owner = resource.getString("owner");
				if (owner.contains("\0")||owner.startsWith(" ")||owner.endsWith(" ")){
					return 2;
				}
				if (owner.equals("*")){
					return 2;
				}
				String ezserver=resource.getString("ezserver");
				if (ezserver.contains("\0")||ezserver.startsWith(" ")||ezserver.endsWith(" ")){
					return 2;
				}
				//all fields checked as valid
				
				String[] key1 = {channel,uriString};
				ArrayList<String> key1List = new ArrayList<String>(Arrays.asList(key1));
				String[] key = {owner,channel,uriString};
				ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
				if(Server.hm.get(keyList)!=null && js.getString("command").equals("PUBLISH")){
				}else if(Server.hm1.get(key1List)!=null && js.getString("command").equals("PUBLISH")){return 3;} //cannot publish resource;
				if(Server.hm1.get(key1List)==null && js.getString("command").equals("REMOVE")){return 3;}//cannot remove
				if(Server.hm.get(keyList)!=null && js.getString("command").equals("SHARE")){
				}else if(Server.hm1.get(key1List)!=null && js.getString("command").equals("SHARE")){return 3;}//cannot share				
				if(js.getString("command").equals("PUBLISH") && (uri.getScheme().equals("file"))){
					return 2;
				}else if(js.getString("command").equals("SHARE") && !uri.getPath().isEmpty()){
					File file=new File(uri.getPath().toString());
					if(!file.exists()){
						System.out.println("0");
						return 1;
					}
				}						
			}catch(JSONException e){return 2;}//invalid bc missing some fields in resource
			 catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(JSONException e){
			return 1;//missing resource
		}
		return 0;//success
	}
}
