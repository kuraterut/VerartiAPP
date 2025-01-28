package src.admin.connection.resources;

import src.admin.utils.*;
import src.admin.connection.Connection;

import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.scene.image.*;
import java.util.*;
import java.nio.file.*;
import java.net.*;
import java.io.*;
import java.time.*;

public class ConnectionResources extends Connection{
	public static ArrayList<ResourceInfo> getResourcesList(String token) {
		try{
			getConnection("http://localhost:8000/api/admin/resource/all");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<ResourceInfo> resources = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        for (Object elem : jsonArr) {
	            JSONObject resourceJson = (JSONObject) elem;
	            ResourceInfo resource = new ResourceInfo();
	            resource.setId((Long)resourceJson.get("id"));
	            resource.setName((String)resourceJson.get("name"));
	            resource.setDescription((String)resourceJson.get("description"));
	            resource.setPrice((Double)resourceJson.get("price"));
	            
	            resources.add(resource);
	        }
	        return resources;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Response getResourceById(String token, Long id) {
		try{
			getConnection("http://localhost:8000/api/admin/resource/"+id);

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        int status = connection.getResponseCode();
	        if(status != 200) return new Response(status, "Ошибка");

            ResourceInfo resource = new ResourceInfo();
            
            resource.setId((Long)data.get("id"));
            resource.setName((String)data.get("name"));
            resource.setDescription((String)data.get("description"));
            resource.setPrice((Double)data.get("price"));
       
	        return resource;
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}

	public static ArrayList<ResourceRequestInfo> getResourcesRequestsList(String token) {
		try{
			getConnection("http://localhost:8000/api/admin/resource/request");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<ResourceRequestInfo> requests = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        
	        for (Object elem : jsonArr) {
	            JSONObject requestJson = (JSONObject) elem;
	            ResourceRequestInfo request = new ResourceRequestInfo();
	            request.setId((Long)requestJson.get("id"));
	            Long resourceId = (Long)requestJson.get("resource_id");
	            ResourceInfo resource = (ResourceInfo) getResourceById(token, resourceId);
	            request.setResource(resource);
	            request.setCount((Integer)requestJson.get("count"));
	            request.setStatus((Integer)requestJson.get("status"));

	            switch((Integer)requestJson.get("status")){
	            	case 0:
			            request.setStatusDescription("Отказано");
	            		break;
            		case 1:
            			request.setStatusDescription("Ожидает обработки администратором");
	            		break;
	            	case 2:
	            		request.setStatusDescription("В пути");
	            		break;
	            	case 3:
	            		request.setStatusDescription("Готово к получению");
	            		break;
	            	case 4:
	            		continue;
	            } 
	            
	            requests.add(request);
	        }
	        return requests;
	    }

	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Response createResourceRequest(String token, Long id, int count){
		try{
			getConnection("http://localhost:8000/api/master/resource/request");

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("id", id);
			outJson.put("count", count);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			Response response = new Response();

			if(status == 200) return new Response(200, "");
			else return new Response(status, "Ошибка");
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return new Response();
	    }
	}
}