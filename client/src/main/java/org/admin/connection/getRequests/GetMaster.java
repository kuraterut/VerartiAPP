package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.model.Master;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetMaster extends Connection {
    public static List<Master> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/master");

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<Master> masters = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("masters");

            for(Object elem: jsonArr){
                JSONObject masterJSON = (JSONObject) elem;
                Master master = Master.fromJson(masterJSON);
                masters.add(master);
            }
            return masters;
        }
        catch(Exception ex){
            System.out.println("class: GetMaster, method: getAll, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Master getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/users/master/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            int code = connection.getResponseCode();
            if(code != 200) return new Master(code, getErrorMsg());

            return Master.fromJson(data);

        }
        catch(Exception ex){
            System.out.println("class: GetMaster, method: getById, exception: " + ex.getMessage());
            return new Master(404, ex.getMessage());
        }
    }

    public static List<Master> getListByDate(String token, LocalDate date, boolean appointed){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            String url = "http://localhost:8000/api/admin/appointment/master?";
            url += "date=" + encodedDate + "&appointed=" + appointed;
            getConnection(url);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<Master> masters = new ArrayList<>();
            JSONObject data = getJson();

            JSONArray mastersArr = (JSONArray)data.get("masters");
            for(Object elem: mastersArr){
                JSONObject masterJSON = (JSONObject)elem;
                Master master = Master.fromJson(masterJSON);
                masters.add(master);
            }

            return masters;

        }
        catch(Exception ex){
            System.out.println("class: GetMaster, method: getListByDate, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
