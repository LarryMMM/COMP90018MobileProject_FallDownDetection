package Client;
/*The EZshare client package implements an application that
* client end can use command line to communicate with server.
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/
import org.json.JSONObject;
//transform all messages to JSON form
public class toJSON {

	public static JSONObject publish(String command, JSONObject resource) {
		JSONObject json = new JSONObject();
		json.put("command", command);
		json.put("resource", resource);
		return json;
	}

	public static JSONObject remove(String command, JSONObject resource) {
		JSONObject json = new JSONObject();
		json.put("command", command);
		json.put("resource", resource);
		return json;
	}

	public static JSONObject fetch(String command, JSONObject resource, boolean isTemplate) {
		JSONObject json = new JSONObject();
		if (isTemplate) {
			json.put("command", command);
			json.put("resourceTemplate", resource);
		}
		return json;
	}

	public static JSONObject share(String command, String secret, JSONObject resource) {
		JSONObject json = new JSONObject();
		json.put("command", command);
		json.put("secret", secret);
		json.put("resource", resource);
		return json;
	}

	public static JSONObject query(String command, JSONObject resource) {
		JSONObject json = new JSONObject();
		json.put("command", command);
		json.put("relay", true);
		json.put("resourceTemplate", resource);
		return json;
	}

	public static JSONObject exchange(String command, JSONObject[] serverList) {
		JSONObject json = new JSONObject();
		json.put("serverList", serverList);
		json.put("command", command);
		return json;
	}
	
	public static JSONObject Invalid() {
		JSONObject json = new JSONObject();
		json.put("resource", "error");
		json.put("errorMessage", "invalid command");
		return json;
	}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public static JSONObject subscribe(String command, JSONObject resource,String id){
		JSONObject json = new JSONObject();
		json.put("command", command);
		json.put("relay", true);
		json.put("id", id);
		json.put("resourceTemplate", resource);		
		return json;
	}
	public static JSONObject unsubscribe(String id){
		JSONObject json = new JSONObject();
		json.put("command", "SUBSCRIBE");
		json.put("id", id);	
		return json;
	}
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
