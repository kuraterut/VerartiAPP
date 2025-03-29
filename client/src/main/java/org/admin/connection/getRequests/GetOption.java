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
            JSONArray optionsArray = (JSONArray) data.get("options");
            for (Object optionObj : optionsArray) {
                JSONObject optionJSON = (JSONObject) optionObj;
                Option option = Option.fromJson(optionJSON);
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
            return Option.fromJson(data);

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
            List<Option> options = new ArrayList<>();
            JSONArray optionsArray = (JSONArray) data.get("options");
            for (Object optionObj : optionsArray) {
                JSONObject optionJSON = (JSONObject) optionObj;
                Option option = Option.fromJson(optionJSON);
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
