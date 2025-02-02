package org.director.connection;

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

	public static ArrayList<String[]> getResourcesList(String token) {
		try{
			getConnection("http://localhost:8000/api/master/resource/all");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<String[]> result = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[3];
	            strArr[0] = String.valueOf((int)jsonObjArr.get("id"));
	            strArr[1] = (String)jsonObjArr.get("name");
	            strArr[2] = (String)jsonObjArr.get("description");
	            result.add(strArr);
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static ArrayList<String[]> getResourcesRequestsList(String token) {
		try{
			getConnection("http://localhost:8000/api/master/resource/request");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
	        JSONObject data = getJson();

	        ArrayList<String[]> result = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[4];
	            strArr[0] = String.valueOf((int)jsonObjArr.get("id"));
	            strArr[1] = (String)jsonObjArr.get("name");
	            strArr[2] = String.valueOf((int)jsonObjArr.get("count"));
	            int status = (int)jsonObjArr.get("status");
	            switch(status){
	            	case 0:
	            		strArr[3] = "Отказано";
	            		break;
            		case 1:
            			strArr[3] = "Ожидает обработки администратором";
	            		break;
	            	case 2:
            			strArr[3] = "В пути";
	            		break;
	            	case 3:
            			strArr[3] = "Готово к получению";
	            		break;
	            	case 4:
	            		continue;
	            } 
	            
	            result.add(strArr);
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static Map<String, String> getProfileInfo(String token){
		try{
			getConnection("http://localhost:8000/api/master/profile/");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			Map<String, String> masterInfoMap = new HashMap<>();
			JSONObject data = getJson();

			masterInfoMap.put("name", (String)data.get("name"));
			masterInfoMap.put("surname", (String)data.get("surname"));
			masterInfoMap.put("patronymic", (String)data.get("patronymic"));
			masterInfoMap.put("phone", (String)data.get("phone"));
			masterInfoMap.put("email", (String)data.get("email"));
			masterInfoMap.put("birhday", (String)data.get("birthday"));
			masterInfoMap.put("bio", (String)data.get("bio"));
			masterInfoMap.put("role", (String)data.get("role"));
			masterInfoMap.put("photo", (String)data.get("photo"));
			masterInfoMap.put("current_salary", (String)data.get("current_salary"));
			
			return masterInfoMap;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static int changeProfileInfo(String token, String name, String surname, String patronymic, String email, String phone, String bio){
		try{
			getConnection("http://localhost:8000/api/master/profile/info");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("name", name);
			outJson.put("surname", surname);
			outJson.put("patronymic", patronymic);
			outJson.put("email", email);
			outJson.put("phone", phone);
			outJson.put("bio", bio);

			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static int createResourceRequest(String token, int id, int count){
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

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static String[] getResourceInfoByID(String token, int id){
		try{
			getConnection("http://localhost:8000/api/master/resource/"+id);

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);

			
			JSONObject data = getJson();
			String[] result = new String[2];
			result[0] = (String)data.get("name");
			result[1] = (String)data.get("description");

			return result;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static int changeProfilePassword(String token, String oldPassword, String newPassword){
		try{
			getConnection("http://localhost:8000/api/master/profile/password");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("oldPassword", oldPassword);
			outJson.put("newPassword", newPassword);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}

	public static int changeProfilePhoto(String token, File file){
		try{
			getConnection("http://localhost:8000/api/master/profile/photo");

			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			byte[] byteArray = Files.readAllBytes(file.toPath());
			String encodedPhoto = Base64.getEncoder().encodeToString(byteArray);
			outJson.put("photo", encodedPhoto);
			
			sendJson(outJson);
			
			int status = connection.getResponseCode();

			return status;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return 1;
	    }
	}
	
	public static Image getProfilePhoto(String avatarBytesStr){
		try{
			if(avatarBytesStr == null){
				return new Image(new FileInputStream("client/photos/standard.jpg"));
			}
			Image avatarImage = null;
			FileOutputStream avatarFile = new FileOutputStream("client/photos/avatar.jpg");
	        
			byte[] avatarBytes = Base64.getDecoder().decode(avatarBytesStr);

			for(int i = 0; i < avatarBytes.length; i++){
				avatarFile.write((int)avatarBytes[i]);
			}
	        
	        avatarFile.close();
	        avatarImage = new Image(new FileInputStream("client/photos/avatar.jpg"));
	        return avatarImage;
		}
		catch(Exception ex){
			System.out.println(ex);
			return null;
		}
	}



	public static String[][] getTimetableByYM(int year, int month, String token){
		try{
			getConnection("http://localhost:8000/api/master/shedule/month");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);

			sendJson(outJson);

	        JSONObject data = getJson();

	        String[][] result = new String[32][2];

	        JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[2];
	            String dayNumStr = ((String)jsonObjArr.get("date")).substring(0, 2);
	            int dayIndex = Integer.parseInt(dayNumStr)-1;

	            strArr[0] = (String)jsonObjArr.get("count");
	            strArr[1] = (String)jsonObjArr.get("time");
	            result[dayIndex] = strArr;
	            
	        }
	        return result;

	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static ArrayList<String[]> getTimetableByDate(int year, int month, int day, String token){
		try{
			getConnection("http://localhost:8000/api/master/shedule/day");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);
			outJson.put("day", day);

			sendJson(outJson);

			ArrayList<String[]> result = new ArrayList<>();
			JSONObject data = getJson();
			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[3];
	            strArr[0] = (String)jsonObjArr.get("name");
	            strArr[1] = (String)jsonObjArr.get("time");
	            strArr[2] = (String)jsonObjArr.get("service");
	            result.add(strArr);
	        }

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