package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.Admin;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetAdmin extends Connection {
    public static List<Admin> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/admin");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            List<Admin> admins = new ArrayList<>();
            JSONArray adminsJSON = (JSONArray) data.get("admins");
            System.out.println(data);
            for(Object adminObj : adminsJSON){
                JSONObject adminJSON = (JSONObject) adminObj;
                Admin admin = Admin.fromJson(adminJSON);
                admins.add(admin);
            }
            return admins;
        }
        catch (Exception ex){
            System.out.println("class: GetAdmin, method: getAll, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Admin getByDate(String token, LocalDate date){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/appointment/admin?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            int code = connection.getResponseCode();
            if(code != 200) return null;

            return Admin.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetAdmin, method: getByDate, exception: " + ex.getMessage());
            return null;
        }
    }

    public static Admin getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/users/admin/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            return Admin.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetAdmin, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }
}
