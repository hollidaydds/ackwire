package ackwire;

import java.util.Map;

import org.json.JSONObject;

public class JSONMaker {
	

	public static JSONObject makeLoginMessage(String username){
		
		JSONObject message = new JSONObject();
		message.put("username",username);
	
		JSONObject outerMessage = new JSONObject();	
		outerMessage.put("type","login");
		outerMessage.put("message",message);
		
		return outerMessage;
		
	}
	
	public static JSONObject makeGlobalChatMessage(String text){
		
		JSONObject outerMessage = new JSONObject();	
		outerMessage.put("type","chat");
		outerMessage.put("message",text);
		
		return outerMessage;
		
	}
	
	
	public static JSONObject makeInternalChatMessage(String text, String username){
	
		JSONObject message = new JSONObject();	
		message.put("gameAction","chat");
		message.put("username",username);
		message.put("chat",text);
		
		return message;
		
	}
	
	public static JSONObject makeServerMessage(String type, Map<String,String> fields, String username){
		
		JSONObject message = new JSONObject();
		message.put("gameAction",type);
		message.put("module","acquire");
		message.put("username",username);
		//message.put("app","acquire");
		
		for(String s : fields.keySet()){
			message.put(s,fields.get(s));
		}
		
		return message;
	}
	
	//overloaded version
	public static JSONObject makeServerMessage(String type, Map<String,String> fields, Map<String,JSONObject> objects,  String username){
		
		JSONObject message = new JSONObject();
		message.put("gameAction",type);
		message.put("module","acquire");
		message.put("username",username);
		//message.put("app","acquire");
		
		for(String s : fields.keySet()){
			message.put(s,fields.get(s));
		}
		
		for(String s : objects.keySet()){
			message.put(s,objects.get(s));
		}
		
		return message;
	}
	
	
	public static JSONObject makeApplicationMessage(String type, Map<String,String> fields, String username){
		
		JSONObject message = new JSONObject();
		message.put("gameAction",type);
		message.put("module","acquire");
		message.put("username",username);
		//message.put("app","acquire");
		
		for(String s : fields.keySet()){
			message.put(s,fields.get(s));
		}
		
		JSONObject outerMessage = new JSONObject();
		
		outerMessage.put("type","application");
		outerMessage.put("action", "broadcast");
		outerMessage.put("module",  "acquire");
		outerMessage.put("message",message);
		
		return outerMessage;
		
		
	}

}
