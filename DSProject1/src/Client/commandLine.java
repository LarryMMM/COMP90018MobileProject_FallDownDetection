package Client;
import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;

public class commandLine {
	    // put command line input to resource form
	public static JSONObject getResourceFcmd(CommandLine cmd) {
		clientResource resource = new clientResource();
		try {
			String name = "",description = "",uri = "",channel = "",owner = "",ezserver = "";
			String[] tags = {};
			if (cmd.getOptionValue("name") != null) {name = cmd.getOptionValue("name");}
			if (cmd.getOptionValue("description") != null) {description = cmd.getOptionValue("description");}
			if (cmd.getOptionValue("tags") != null) {tags = cmd.getOptionValue("tags").split(",");}
        	if (cmd.getOptionValue("uri") != null) {uri = cmd.getOptionValue("uri");}
			if (cmd.getOptionValue("channel") != null) {channel = cmd.getOptionValue("channel");}
			if (cmd.getOptionValue("owner") != null) {owner = cmd.getOptionValue("owner");}

			resource = new clientResource(name, description, tags, uri, channel, owner, ezserver);
			
		} catch (NullPointerException e) {}
		return resource.toJsonForm();
	}
	
    //check command options and return correct JSON form message for transmission
	public static JSONObject checkOptions(CommandLine cmd){

		JSONObject resourceTemp = getResourceFcmd(cmd);
		JSONObject jsonObject = new JSONObject();
		JSONObject serverlist = new JSONObject();
		JSONObject errorMsg = new JSONObject();
		
		if (cmd.hasOption("publish")) {
			jsonObject = toJSON.publish("PUBLISH", resourceTemp);
		} else if (cmd.hasOption("remove")) {
			jsonObject = toJSON.remove("REMOVE", resourceTemp);
		} else if (cmd.hasOption("share")) {
			String secret = "";
			if (cmd.getOptionValue("secret") != null) {
				secret = cmd.getOptionValue("secret");
				jsonObject = toJSON.share("SHARE", secret, resourceTemp);
			} else {System.out.println("Plese enter secret");}
		} else if (cmd.hasOption("query")) {
			jsonObject = toJSON.query("QUERY", resourceTemp);
		} else if (cmd.hasOption("fetch")) {
			jsonObject = toJSON.fetch("FETCH", resourceTemp, true);
		} else if (cmd.hasOption("exchange")) {
			if (serverlist != null) {
				String[] temp = cmd.getOptionValue("servers").split(",");
				JSONObject[] serverList = new JSONObject[temp.length];
	
				for (int i = 0; i < temp.length; i++) {
					String[] content = new String[2];
					serverList[i] = new JSONObject();
					content = temp[i].split(":");
					serverList[i].put("hostname", content[0]);
					serverList[i].put("port", content[1]);
				}
				jsonObject = toJSON.exchange("EXCHANGE", serverList);
			}
		} 
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		else if(cmd.hasOption("subscribe")){		
			String id="";	
			if(cmd.getOptionValue("id")!=null){id=cmd.getOptionValue("id");}			
			jsonObject=toJSON.subscribe("SUBSCRIBE",resourceTemp,id);
			
		}
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		else {
			errorMsg = toJSON.Invalid();
			System.out.println(errorMsg);
	        jsonObject = resourceTemp;
		}
		return jsonObject;
	}
	
	


}
