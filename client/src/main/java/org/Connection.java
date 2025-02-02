package org;

import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.scene.image.*;
import java.util.*;
import java.nio.file.*;
import java.net.*;
import java.io.*;

public class Connection{
	private static HttpURLConnection connection;
	
	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}

	public static String[] checkAuthAndGetToken(String login, String password, String role){
		try{
			getConnection("http://localhost:8000/auth/signin");

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("phone", login);
			outJson.put("password", password);
			outJson.put("role", role);

			sendJson(outJson);

			JSONObject data = getJson();  


			String msgText = "";
			String token = "";
			String[] result = new String[2];
	        int status = connection.getResponseCode();

	        switch(status){
	        	case 401:
	        		token = "-1";
	        		msgText = "Неверный логин или пароль";
	        		break;
	        		
	        	case 500:
	        		token = "-1";
	        		msgText = "Неверный формат ввода";
	        		break;

	        	case HttpURLConnection.HTTP_OK:
			        token = (String)data.get("token");
			        break;
	        	
	        	default:
	        		token = "-1";
	        		msgText = "Неизвестная ошибка";
	        		break;
	        }

	        result[0] = token;				
	        result[1] = msgText;
	        return result;
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	private static void sendJson(JSONObject outJson){
		try{
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        writer.write(outJson.toString());
	        writer.flush();
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return;
	    }
	}


	private static JSONObject getJson(){
		try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

	        String line;
	        StringBuilder response = new StringBuilder();

	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }

	        reader.close();

	        String responseStr = response.toString();

	        Object obj = new JSONParser().parse(responseStr);
	        return (JSONObject) obj;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}
}