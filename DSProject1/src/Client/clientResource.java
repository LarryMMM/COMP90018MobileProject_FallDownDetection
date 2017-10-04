package Client;
/*The EZshare client package implements an application that
* client end can use command line to communicate with server.
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import org.json.*;
//client end resource template
public class clientResource {

	String name;
	String description;
	String[] tags;
	String uri;
	String channel;
	String owner;
	String ezserver;
	
	public clientResource(){
		this.name = "";
		this.description = "";
		this.tags = null;
		this.uri = "";
		this.channel = "";
		this.owner = "";
		this.ezserver = "";
	}
	
	public clientResource(String name,String description,String[] tags,String uri,String channel,String owner,String ezserver){
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.uri = uri;
		this.channel = channel;
		this.owner = owner;
		this.ezserver = ezserver;
	}
	
	public JSONObject toJsonForm(){
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("description", description);
		json.put("tags", tags);
		json.put("uri", uri);
		json.put("channel", channel);
		json.put("owner", owner);
		json.put("ezserver", ezserver);
		return json;
	}
	
}
