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

	public static JSONObject getMasterResourcesListJSON() {
		try{
			getConnection("http://localhost:8000/api/master/resource");

			connection.setRequestMethod("GET");

	        return getJsonWithoutOut();
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static JSONObject checkAuthAndGetToken(String login, String password){
		try{
			getConnection("http://localhost:8000/api/master/resource");

			connection.setRequestMethod("GET");
			connection.setDoOutput(true);

			return null;        
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static JSONObject getTimetableByYM(int year, int month, String token){
		try{
			getConnection("http://localhost:8000/api/master/resource");

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", token);
			connection.setDoOutput(true);

			JSONObject outJson = new JSONObject();
			outJson.put("year", year);
			outJson.put("month", month);

	        return getJson(outJson);
	    }
	    catch(Exception ex){
	    	System.out.println(ex);
	    	return null;
	    }
	}

	public static JSONObject getTimetableByDate(int year, int month, int day, String token){
		try{
			getConnection("http://localhost:8000/api/master/resource");

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