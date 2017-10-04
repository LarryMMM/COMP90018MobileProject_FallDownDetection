package Server;
import java.io.DataOutputStream;
/*The EZshare server package implements an application that
* .
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
//import java.util.Set;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import Resource.Resource;

public class Functions {
	
	public static void exchangeServerlist(JSONObject command) {
		JSONArray serverList =command.getJSONArray("serverList");
		for (int i=0;i<serverList.length();i++){
			if (!Server.currentServerList.contains(serverList.getJSONObject(i))){
				Server.currentServerList.add(serverList.getJSONObject(i));
			}
		}
	}

	public static String fetchResource(JSONObject command, DataOutputStream fileOutput) throws JSONException, URISyntaxException, IOException {
		JSONObject rssTemp=command.getJSONObject("resourceTemplate");
		//String totalResponse = "";
		JSONObject response = new JSONObject();
		JSONObject resultSize = new JSONObject();
		String channel=rssTemp.getString("channel");
		String[] key={channel,rssTemp.getString("uri")};
		ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
		URI uri=new URI(rssTemp.getString("uri"));
		File file=new File(uri.getPath().toString());
		long resourceSize=0;
		if(file.exists()){
			resourceSize=file.length();
		}else{
			response.put("response", "error");
			response.put("errorMessage", "invalid resourceTemplate");
			return response.toString();
		}
		response.put("response", "success");
		//totalResponse = response.toString();
		fileOutput.writeUTF(response.toString());
		JSONObject tmp=new JSONObject();
		tmp=Server.hm1.get(keyList).toJsonJson();
		tmp.put("resourceSize", resourceSize);
		//totalResponse = totalResponse + "\n" +tmp.toString();
		fileOutput.writeUTF(tmp.toString());
		if(file.exists()){
			// Send this back to client so that they know what the file is.
			try {
				// Start sending file
				RandomAccessFile byteFile = new RandomAccessFile(file,"r");
				byte[] sendingBuffer = new byte[1024];
				int num;
				// While there are still bytes to send..
/*				while((num = byteFile.read(sendingBuffer)) > 0){
					//System.out.println(num);
					fileOutput.write(Arrays.copyOf(sendingBuffer, num));
					fileOutput.flush();
				}*/
			    byte[] buf = new byte[1024];
			    int r;
			    while ((r = byteFile.read(buf)) >= 0) {
			    	fileOutput.write(buf, 0, r);
			    }
				byteFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultSize.put("resultSize", 1).toString();
		//System.out.println(totalResponse);
	}

	public static ArrayList<Resource> queryResource(JSONObject command) throws JSONException, URISyntaxException {	
		System.out.println(Arrays.asList(Server.hm));
		//System.out.println("circle one query");
		JSONObject rssTmp=command.getJSONObject("resourceTemplate");
		String name=rssTmp.getString("name");
		String description=rssTmp.getString("description");
		JSONArray tmp =rssTmp.getJSONArray("tags");
		String[] tags=new String[tmp.length()];
		for(int i=0;i<tmp.length();i++){tags[i]=tmp.getString(i);}
		URI uri=new URI(rssTmp.getString("uri"));		
		String channel=rssTmp.getString("channel");
		String owner="";
		if(!rssTmp.getString("owner").isEmpty()){
			owner=rssTmp.getString("owner");
		}
		String ezserver=rssTmp.getString("ezserver");
		Resource rssTemplate=new Resource(name,description,tags,uri.toString(),channel,owner,ezserver);
		int resultSize=0;
		ArrayList<Resource> result=new ArrayList<Resource>();
		
		for(ArrayList<String> key1 :Server.hm.keySet()){
			//System.out.println("ketset circle");
			Resource kk=queryReturnResource(rssTemplate,Server.hm.get(key1));
			if(kk==null){
				System.out.println("match query inner circle");
				continue;
				}
			else{
				//System.out.println("match query inner circle02");
				resultSize=resultSize+1;
				result.add(kk);
			}			
		}
		System.out.println("result"+result);
		return result;
	}
	public static Resource queryReturnResource(Resource rssTemplate,Resource rssExist){
		//System.out.println("testing query");	
/*		if(rssTemplate.getChannel().isEmpty()&&rssTemplate.getDescription().isEmpty()&&rssTemplate.getEZserver().isEmpty()
				&&rssTemplate.getName().isEmpty()&&rssTemplate.getOwner().isEmpty()&&rssTemplate.getUri().isEmpty()&&
				Arrays.asList(rssTemplate.getTags()).isEmpty()){			
			if(!rssExist.getOwner().isEmpty()){
				Resource changeOwnerToStar=rssExist;
				changeOwnerToStar.setOwner("*");
				return changeOwnerToStar;
			}else{
				return rssExist;
			}			
		}else if(rssTemplate.getChannel().equals(rssExist.getChannel())//channel match
				&& rssTemplate.getOwner().equals(rssExist.getOwner())
				&& Arrays.asList(rssExist.getTags()).containsAll(Arrays.asList(rssTemplate.getTags()))
				&& rssTemplate.getUri().equals(rssExist.getUri())
				&& ((rssTemplate.getName().isEmpty()&&rssTemplate.getDescription().isEmpty())||
						(!rssTemplate.getDescription().isEmpty()&&rssExist.getDescription().contains(rssTemplate.getDescription()))||
						(!rssTemplate.getName().isEmpty() && rssExist.getName().contains(rssTemplate.getName())))			
				){
					if(!rssExist.getOwner().isEmpty()){
						Resource changeOwnerToStar=rssExist;
						changeOwnerToStar.setOwner("*");
						return changeOwnerToStar;
					}else{
						return rssExist;
					}			
				}*/
		
		if(	(rssTemplate.getChannel().isEmpty() || rssTemplate.getChannel().equals(rssExist.getChannel()))//channel match
			&& (rssTemplate.getOwner().isEmpty() || rssTemplate.getOwner().equals(rssExist.getOwner()))//owner match
			&& (Arrays.asList(rssTemplate.getTags()).isEmpty()||
					Arrays.asList(rssTemplate.getTags()).containsAll(Arrays.asList(rssExist.getTags())) )//tags contain
			&& (rssTemplate.getUri().isEmpty() || rssTemplate.getUri().equals(rssExist.getUri()) )//uri match
			&& ((rssTemplate.getName().isEmpty()&&rssTemplate.getDescription().isEmpty())||	
				rssExist.getName().contains(rssTemplate.getName())  ||
				rssExist.getDescription().contains(rssTemplate.getDescription())
				)
		){		
			//System.out.println("query match");
			if(!rssExist.getOwner().isEmpty()){
				Resource changeOwnerToStar=new Resource(rssExist);
				changeOwnerToStar.setOwner("*");
				return changeOwnerToStar;
			}else{
				return rssExist;
			}
		}
		//System.out.println("query dismatch");
		return null;		
	} 
	public static void shareResource(JSONObject command) throws URISyntaxException {
		JSONObject rss=command.getJSONObject("resource");
		String name=rss.getString("name");
		String description=rss.getString("description");
		
		JSONArray tmp =rss.getJSONArray("tags");
		String[] list=new String[tmp.length()];
		for(int i=0;i<tmp.length();i++){list[i]=tmp.getString(i);}
/*		
		String[] list = (String[]) rss.get("tags");*/
		String str=rss.getString("uri");
		URI uri=new URI(str);
		String channel=rss.getString("channel");
		String owner=rss.getString("owner");
		//String EZserver=rss.getString("ezserver");
		String EZserver = Server.hostname+":"+Server.port;
		Resource rss2=new Resource(name,description,list,uri.toString(),channel,owner,EZserver);
		String[] key = {owner,channel,uri.toString()};
		String[] key1= {channel,uri.toString()};
		ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
		ArrayList<String> key1List = new ArrayList<String>(Arrays.asList(key1));
		Server.hm.put(keyList,rss2);
		Server.hm1.put(key1List, rss2);
		//System.out.println(rss2.toJson());
	}

	public static String removeResource(JSONObject command) throws URISyntaxException {
		JSONObject resource = command.getJSONObject("resource");
		String owner = resource.getString("owner");
		String channel = resource.getString("channel");
		String str=resource.getString("uri");
		URI uri=new URI(str);
		String[] key = {owner,channel,uri.toString()};
		ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
		String[] key1 = {channel,uri.toString()};
		ArrayList<String> key1List = new ArrayList<String>(Arrays.asList(key1));
		//System.out.println("1");
		//System.out.println(Arrays.asList(Server.hm));
		
		if (Server.hm.containsKey(keyList)){
			Server.hm.remove(keyList);
			Server.hm1.remove(key1List);
			JSONObject response=new JSONObject();
			response.put("response", "success");
			return response.toString();
		}else{
			JSONObject response=new JSONObject();
			response.put("response", "error");
			response.put("errorMessage", "cannot remove resource");
			return response.toString();
		}
	}

	public static void publishResource(JSONObject js) throws URISyntaxException{
		JSONObject rss=js.getJSONObject("resource");
		String name=rss.getString("name");
		String description=rss.getString("description");
		//String[] list = (String[]) rss.get("tags");
		JSONArray tmp =rss.getJSONArray("tags");
		String[] list=new String[tmp.length()];
		for(int i=0;i<tmp.length();i++){list[i]=tmp.getString(i);}
		String str=rss.getString("uri");
		URI uri=new URI(str);
		String channel=rss.getString("channel");
		String owner=rss.getString("owner");
		String EZserver = Server.hostname+":"+Server.port;
		Resource rss2=new Resource(name,description,list,uri.toString(),channel,owner,EZserver);
		String[] key = {owner,channel,uri.toString()};
		String[] key1= {channel,uri.toString()};
		ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key));
		ArrayList<String> key1List = new ArrayList<String>(Arrays.asList(key1));
		Server.hm.put(keyList,rss2);
		Server.hm1.put(key1List, rss2);
		System.out.println(rss2.toJsonJson().toString());
		//Set<String[]> keySet = Server.hm.keySet();
		//System.out.println(Server.hm.keySet());
		System.out.println(Arrays.asList(Server.hm));
	}
	
	/*this function is to get options from the command line to change the host name, connection interval limit,
	exchange interval,port,secret or show the messages to debug.*/
public static CommandLine getOptions(String[] args) {
		
		Options options = new Options();

		options.addOption("advertisedhostname", true, "advertised hostname");
		options.addOption("connectionintervallimit", true, "connection interval limit in seconds");
		options.addOption("exchangeinterval", true, "exchange interval in seconds");
		options.addOption("port", true, "server port, an integer");
		options.addOption("secret", true, "secret");
		options.addOption("debug", false, "debug");
		options.addOption("", false, "Invalid command line");
		

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (NullPointerException e) {
			
		} catch (ParseException e) {
			
		}
		return cmd;
	}

/* this function is to check if the String can be changed to integer.*/
public static boolean isInteger(String str){
	Pattern pattern = Pattern.compile("[0-9]*");
	return pattern.matcher(str).matches();
	
}
}
