package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.Option;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GetOption extends Connection {
    public static List<Option> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/option/");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            List<Option> options = new ArrayList<>();
            JSONArray servicesArray = (JSONArray) data.get("options");
            for (Object serviceObj : servicesArray) {
                JSONObject serviceJSON = (JSONObject) serviceObj;
                Option option = new Option();

                Long id = (Long) serviceJSON.get("id");
                String name = (String) serviceJSON.get("name");
                String description = (String) serviceJSON.get("description");
                Long price = (Long) serviceJSON.get("price");
                String[] durationStr = ((String)serviceJSON.get("duration")).split(":");
                LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

                option.setId(id);
                option.setName(name);
                option.setDescription(description);
                option.setPrice(price);
                option.setDuration(duration);

                options.add(option);
            }
            return options;

        }catch (Exception ex){
            System.out.println("class: GetOption, method: getAll, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Option getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/option/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();


            Option option = new Option();
            String name = (String)data.get("name");
            String description = (String)data.get("description");
            Long price = (Long)data.get("price");
            String[] durationStr = ((String)data.get("duration")).split(":");
            LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

            option.setId(id);
            option.setName(name);
            option.setDescription(description);
            option.setPrice(price);
            option.setDuration(duration);

            return option;

        }
        catch(Exception ex){
            System.out.println("class: GetOption, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }
    public static List<Option> getListByMasterId(String token, Long masterId){
        try{
            getConnection("http://localhost:8000/api/admin/option/master?master_id=" + masterId);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            System.out.println(data);
            List<Option> options = new ArrayList<>();
            JSONArray optionsArray = (JSONArray) data.get("options");
            for (Object serviceObj : optionsArray) {
                JSONObject serviceJSON = (JSONObject) serviceObj;
                Option option = new Option();

                Long id = (Long) serviceJSON.get("id");
                String name = (String) serviceJSON.get("name");
                String description = (String) serviceJSON.get("description");
                Long price = (Long) serviceJSON.get("price");
                String[] durationStr = ((String)serviceJSON.get("duration")).split(":");
                LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

                option.setId(id);
                option.setName(name);
                option.setDescription(description);
                option.setPrice(price);
                option.setDuration(duration);

                options.add(option);
            }
            return options;

        }
        catch(Exception ex){
            System.out.println("class: GetOption, method: getListByMasterId, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
