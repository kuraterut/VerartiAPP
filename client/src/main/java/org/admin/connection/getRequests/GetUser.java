package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.User;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetUser extends Connection {
    public static List<User> getAllAdmins(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/admin");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            List<User> admins = new ArrayList<>();
            JSONArray adminsJSON = (JSONArray) data.get("admins");
            for(Object adminObj : adminsJSON){
                JSONObject adminJSON = (JSONObject) adminObj;
                User admin = User.fromJson(adminJSON);
                admins.add(admin);
            }
            return admins;
        }
        catch (Exception ex){
            System.out.println("class: GetUser, method: getAllAdmins, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<User> getAllMasters(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/master");

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<User> masters = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("masters");

            for(Object elem: jsonArr){
                JSONObject masterJSON = (JSONObject) elem;
                User master = User.fromJson(masterJSON);
                masters.add(master);
            }
            return masters;
        }
        catch(Exception ex){
            System.out.println("class: GetUser, method: getAllMasters, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static User getAdminByDate(String token, LocalDate date){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/appointment/admin?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            int code = connection.getResponseCode();
            if(code == 404) return new User(404, "Не назначен");
            if(code != 200) return new User(code, getErrorMsg());
            return User.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetUser, method: getAdminByDate, exception: " + ex.getMessage());
            return new User(404, ex.getMessage());
        }
    }

    public static User getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/users/admin/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            int code = connection.getResponseCode();
            if (code != 200) return new User(code, getErrorMsg());

            return User.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetUser, method: getById, exception: " + ex.getMessage());
            return new User(404, ex.getMessage());
        }
    }

    public static User getByPhone(String token, String phone){
        try{
            String encodedPhone = URLEncoder.encode(phone, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/users/phone?phone=" + encodedPhone+"&role=ADMIN");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            int code = connection.getResponseCode();
            if (code != 200) return new User(code, getErrorMsg());
            return User.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetUser, method: getByPhone, exception: " + ex.getMessage());
            return new User(404, ex.getMessage());
        }
    }

    public static List<User> getMastersByDate(String token, LocalDate date, boolean appointed){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            String url = "http://localhost:8000/api/admin/appointment/master?";
            url += "date=" + encodedDate + "&appointed=" + appointed;
            getConnection(url);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<User> masters = new ArrayList<>();
            JSONObject data = getJson();

            JSONArray mastersArr = (JSONArray)data.get("masters");
            for(Object elem: mastersArr){
                JSONObject masterJSON = (JSONObject)elem;
                User master = User.fromJson(masterJSON);
                masters.add(master);
            }

            return masters;

        }
        catch(Exception ex){
            System.out.println("class: GetUser, method: getMastersByDate, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
