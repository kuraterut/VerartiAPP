package org.admin.connection.profile;

import org.admin.utils.*;
import org.admin.connection.Connection;

import org.admin.utils.entities.Admin;
import org.json.simple.*;
import javafx.scene.image.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class ConnectionProfile extends Connection{
	public static Response getProfileInfo(String token){
		try{
			getConnection("http://localhost:8000/api/admin/profile/");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			JSONObject data = getJson();

			int status = connection.getResponseCode();
	        if(status != 200) return new Response(status, "Ошибка");

			Admin admin = new Admin();
			
			admin.setId((Long) data.get("id"));
			admin.setName((String) data.get("name"));
			admin.setSurname((String) data.get("surname"));
			admin.setPatronymic((String) data.get("patronymic"));
			admin.setEmail((String) data.get("email"));
			admin.setPhone((String) data.get("phone"));
			admin.setBio((String) data.get("bio"));
			
			admin.setCode(200);
			admin.setMsg("");

			return admin;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}

	public static Response changeProfileInfo(String token, Admin admin){
		try{
			getConnection("http://localhost:8000/api/admin/profile/info");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("name", admin.getName());
			outJson.put("surname", admin.getSurname());
			outJson.put("patronymic", admin.getPatronymic());
			outJson.put("email", admin.getEmail());
			outJson.put("phone", admin.getPhone());
			outJson.put("bio", admin.getBio());

			sendJson(outJson);
			
			int status = connection.getResponseCode();
			if(status == 200) return new Response(200, "");
			else return new Response(status, "Ошибка");
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}


	public static Response changeProfilePassword(String token, String oldPassword, String newPassword){
		try{
			getConnection("http://localhost:8000/api/admin/profile/password");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("oldPassword", oldPassword);
			outJson.put("newPassword", newPassword);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			if(status == 200) return new Response(200, "");
			else return new Response(status, "Ошибка");

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}


	public static Response changeProfilePhoto(String token, File file){
		try{
			getConnection("http://localhost:8000/api/admin/profile/photo");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			byte[] byteArray = Files.readAllBytes(file.toPath());
			String encodedPhoto = Base64.getEncoder().encodeToString(byteArray);
			outJson.put("photo", encodedPhoto);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();
			if(status == 200) return new Response(200, "");
			else return new Response(status, "Ошибка");

		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}


	public static Image getProfilePhoto(String avatarURL){
		try{
			getConnection(avatarURL);
				
			String avatarFileName = "client/photos/"+"avatar.png";
			//TODO получить filename из URL

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			BufferedWriter writter = new BufferedWriter(new FileWriter(avatarFileName));

			String line;

	        while (!(line = reader.readLine()).equals("exit")) {
	            writter.write(line);
	        }

	        Image avatarImage = new Image(new FileInputStream("client/photos/avatar.png"));
	        return avatarImage;
		}
		catch(Exception ex){
			System.out.println(ex);
			return null;
		}
	}

} 