package org.admin.connection;

import org.admin.model.AuthResponse;
import org.admin.utils.*;

import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.scene.image.*;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.*;
import java.net.*;
import java.io.*;
import java.time.*;

public class Connection{
	public static HttpURLConnection connection;

	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}

	public static void sendJson(JSONObject outJson){
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


	public static JSONObject getJson(){
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

	public static String getErrorMsg(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			return response.toString();
		}
		catch(Exception ex){
			System.out.println(ex);
			return "";
		}
	}

	public static AuthResponse checkAuthAndGetToken(String login, String password, UserRole role){
		try{
			getConnection("http://localhost:8000/auth/signin");

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("phone", login);
			outJson.put("password", password);
			outJson.put("role", role.toString());

			sendJson(outJson);

			JSONObject data = getJson();

			AuthResponse authResponse = new AuthResponse();
			int status = connection.getResponseCode();
			if(status != 200){
				authResponse.setCode(status);
				authResponse.setMsg(getErrorMsg());
				return authResponse;
			}
			String token = (String) data.get("token");
			authResponse.setCode(200);
			authResponse.setAuthToken(token);
			return authResponse;
		}
		catch(Exception ex){
			System.out.println("Class Connection, method checkAuthAndGetToken, exception:" + ex.getMessage());
			AuthResponse authResponse = new AuthResponse();
			authResponse.setCode(404);
			authResponse.setMsg(ex.getMessage());
			return authResponse;
		}
	}
}