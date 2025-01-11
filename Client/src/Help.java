import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.*;

public class Help {
    public static void main(String[] args) throws Exception {
        // String id = postReq();
        String get = getReq();
        parseJson(get);
    }

    public static void parseJson(String jsonString) throws Exception{
        
        Object obj = new JSONParser().parse(jsonString);
        
        JSONObject jo = (JSONObject) obj;
        JSONArray arr = (JSONArray) jo.get("data");
        Iterator itr = arr.iterator();
        System.out.println("data:");
        // Выводим в цикле данные массива
        while (itr.hasNext()) {
            JSONObject test = (JSONObject) itr.next();
            System.out.println("name: " + test.get("name") + ", description: " + test.get("description"));
        }
    }

    public static String postReq() throws Exception{
        URL url = new URL("http://localhost:8000/api/admin/resource");

        // Создаем объект HttpURLConnection и настраиваем его
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Создаем тело запроса
        String body = "{\"name\":\"name2\", \"description\": \"description2\"}";

        // Записываем тело запроса в поток вывода
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.flush();

        // Считываем ответ от сервера
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Выводим ответ от сервера
        return response.toString();
        
    }

    public static String getReq() throws Exception{
        URL url = new URL("http://localhost:8000/api/master/resource");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseContent = new StringBuilder();
        String inputLine;

        while ((inputLine = reader.readLine()) != null) {
            responseContent.append(inputLine);
        }
        reader.close();

        return responseContent.toString();
        
    }
}
