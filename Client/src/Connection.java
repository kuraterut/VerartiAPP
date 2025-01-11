import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

public class Connection{
	private static HttpURLConnection connection;

	public static void getConnection(String urlAddr) throws Exception{
		URL url = new URL(urlAddr);
        connection = (HttpURLConnection) url.openConnection();
	}

	public static ArrayList<String[]> getMasterResourcesListJSON(String token) {
		try{
			getConnection("http://localhost:8000/api/master/resource");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", token);

	        JSONObject data = getJsonWithoutOut();

	        ArrayList<String[]> result = new ArrayList<>();

			JSONArray jsonArr = (JSONArray) data.get("data");
	        Iterator itr = jsonArr.iterator();
	        while (itr.hasNext()) {
	            JSONObject jsonObjArr = (JSONObject) itr.next();
	            String[] strArr = new String[2];
	            strArr[0] = (String)jsonObjArr.get("name");
	            strArr[1] = (String)jsonObjArr.get("description");
	            result.add(strArr);
	        }
	        return result;

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

			
			JSONObject data = getJson(outJson);  
			

			
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
	        		
	        }
	        text = "";
	        token = (String)data.get("token");

	        result[0] = token;
	        result[1] = text;
	        return result;
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

			connection.setRequestProperty("Authorization", token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);

	        JSONObject data = getJson(outJson);


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

	public static JSONObject getTimetableByDate(int year, int month, int day, String token){
		try{
			getConnection("http://localhost:8000/api/master/shedule/day");

			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);
			outJson.put("day", day);

			return getJson(outJson);
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}



	private static JSONObject getJson(JSONObject outJson){
		try{
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        writer.write(outJson.toString());
	        writer.flush();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        

	        String line;
	        StringBuilder response = new StringBuilder();
	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }
	        reader.close();

	        String responseStr = response.toString();

	        System.out.println(outJson);
	        Object obj = new JSONParser().parse(responseStr);
	        return (JSONObject) obj;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	private static JSONObject getJsonWithoutOut(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuilder responseContent = new StringBuilder();
	        String inputLine;

	        while ((inputLine = reader.readLine()) != null) {
	            responseContent.append(inputLine);
	        }
	        reader.close();

	        String res = responseContent.toString();

	        Object obj = new JSONParser().parse(res);
	        return (JSONObject) obj;
		}
		catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	
}

//INSERT INTO users (name, surname, patronymic, password_hash, email, phone, role_id) VALUES ('Ilia', 'Kurylin', 'Artemovich', '6e73766e6a6e75347538393438767568323968726866656276383339346876756233758758c5deb39e2e1c2077a3999e1cc77b2ed109ea', 'kuraterut@yandex.ru', '+79092762462', 3);