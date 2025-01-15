import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.scene.image.Image;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class Connection{
	private static HttpURLConnection connection;

	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}

	public static ArrayList<String[]> getMasterResourcesList(String token) {
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

	public static ArrayList<String[]> getMasterResourcesRequestsList(String token) {
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

	public static Map<String, String> getMasterProfileInfo(String token){
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

	public static int changeMasterInfo(String token, String name, String surname, String patronymic, String email, String phone, String bio){
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
			
			// int status = connection.getResponseCode();

			return result;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static int changeMasterPassword(String token, String oldPassword, String newPassword){
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

	public static int changeMasterPhoto(String token, File file){
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
	
	public static Image getMasterPhoto(String avatarBytesStr){
		try{
			if(avatarBytesStr == null){
				return new Image(new FileInputStream("photos/standard.jpg"));
			}
			Image avatarImage = null;
			FileOutputStream avatarFile = new FileOutputStream("photos/avatar.jpg");
	        
			byte[] avatarBytes = Base64.getDecoder().decode(avatarBytesStr);

			for(int i = 0; i < avatarBytes.length; i++){
				avatarFile.write((int)avatarBytes[i]);
			}
	        
	        avatarFile.close();
	        avatarImage = new Image(new FileInputStream("photos/avatar.jpg"));
	        return avatarImage;
		}
		catch(Exception ex){
			System.out.println(ex);
			return null;
		}
	}


	public static String[] checkAuthAndGetToken(String login, String password){
		try{
			getConnection("http://localhost:8000/auth/signin");

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("phone", login);
			outJson.put("password", password);

			sendJson(outJson);

			JSONObject data = getJson();  

			
			String text, token;
			String[] result = new String[2];
	        int status = connection.getResponseCode();

	        switch(status){
	        	case 401:
	        		text = "Неверный логин или пароль";
	        		token = "-1";
	        		result[0] = token;
	        		result[1] = text;
	        		return result;
	        		
	        	case 500:
	        		text = "Неверный формат ввода";
	        		token = "-1";
	        		result[0] = token;
	        		result[1] = text;
	        		return result;

	        	case 200:
	        		text = (String)data.get("role");
			        token = (String)data.get("token");
			        result[0] = token;
			        result[1] = text;				
	        		return result;
	        	
	        	default:
	        		text = "Неизвестная ошибка";
	        		token = "-1";
	        		result[0] = token;
	        		result[1] = text;
	        		return result;
	        }
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static ArrayList<String[]> getTimetableByYM(int year, int month, String token){
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

	        ArrayList<String[]> result = new ArrayList<>();

	        JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[2];
	            strArr[0] = (String)jsonObjArr.get("count");
	            strArr[1] = (String)jsonObjArr.get("time");
	            result.add(strArr);
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

//INSERT INTO users (name, surname, patronymic, password_hash, email, phone, role_id) VALUES ('Ilia', 'Kurylin', 'Artemovich', '6e73766e6a6e75347538393438767568323968726866656276383339346876756233758758c5deb39e2e1c2077a3999e1cc77b2ed109ea', 'kuraterut@yandex.ru', '+79092762462', 3);