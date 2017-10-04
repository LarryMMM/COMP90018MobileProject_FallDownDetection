package Resource;
/*The EZshare server package implements an application that
* .
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;

public class Resource {
	String name;
	String description;
	String[] tags;
	String uri;
	String channel;
	String owner;
	String ezserver;
	public Resource(Resource another){
		this.channel=another.channel;
		this.name=another.name;
		this.description=another.description;
		this.ezserver=another.ezserver;
		for(int i=0;i<another.tags.length;i++){
			this.tags[i]=another.tags[i];
		}
		this.owner=another.owner;
		this.uri=another.uri;
	}
	
	public Resource(){//default constructor
		this.name = "";
		this.description = "";
		this.tags = null;
		this.uri = "";
		this.channel = "";
		this.owner = "";
		this.ezserver = "";
	}
	
	public Resource(String name,String description,String[] tags,String uri,String channel,String owner,String EZserver){
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.uri = uri;
		this.channel = channel;
		this.owner = owner;
		this.ezserver = EZserver;
	}
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	public String[] getTags(){
		return tags;
	}
	public String getUri(){
		return uri;
	}
	public String getChannel(){
		return channel;
	}
	public String getOwner(){
		return owner;
	}
	public String getEZserver(){
		return ezserver;
	}
	public void setOwner(String owner){
		this.owner= owner;
	}
	
	public String toJson(){//print resource instance to String
		String obj;
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("description", description);
		//JSONArray tagsArray = new JSONArray();
		JSONObject tagObject = new JSONObject();
		for (int i=0;i<tags.length;i++){
			tagObject.putOnce(i+"", tags[i]);
			//tagsArray.put(i, tagObject);
		}
		json.put("tags", tagObject);
		json.put("uri", uri);
		json.put("channel", channel);
		json.put("owner", owner);
		json.put("ezserver", ezserver);
		obj = json.toString();
		return obj;
	}
	
	public JSONObject toJsonJson(){//print resource instance to JSON
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
	
	public static Resource getResourceFcmd(CommandLine cmd) {//get resource according to commandline
		Resource resource = new Resource();
		try {
			String name = "";
			if (cmd.getOptionValue("name") != null) {
				name = cmd.getOptionValue("name");
			}
			String description = "";
			if (cmd.getOptionValue("description") != null) {
				description = cmd.getOptionValue("description");
			}
			String[] tags = {};
			if (cmd.getOptionValue("tags") != null) {
				tags = cmd.getOptionValue("tags").split(",");
			}

			String uri = "";
			if (cmd.getOptionValue("uri") != null) {
				uri = cmd.getOptionValue("uri");
			}

			String channel = "";
			if (cmd.getOptionValue("channel") != null) {
				channel = cmd.getOptionValue("channel");
			}

			String owner = "";
			if (cmd.getOptionValue("owner") != null) {
				owner = cmd.getOptionValue("owner");
			}
			String ezserver = "";
			resource = new Resource(name, description, tags, uri, channel, owner, ezserver);
		} catch (NullPointerException e) {
		}
		return resource;
	}
}
