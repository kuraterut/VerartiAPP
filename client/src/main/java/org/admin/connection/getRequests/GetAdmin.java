package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.AdminInfo;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GetAdmin extends Connection {
    public static List<AdminInfo> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/admin");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            List<AdminInfo> admins = new ArrayList<>();
            JSONArray adminsJSON = (JSONArray) data.get("admins");
            for(Object adminObj : adminsJSON){
                JSONObject adminJSON = (JSONObject) adminObj;
                AdminInfo admin = new AdminInfo();

                Long id = (Long) data.get("id");
                String name = (String)data.get("name");
                String surname = (String)data.get("surname");
                String patronymic = (String)data.get("patronymic");
                String phone = (String)data.get("phone");
                String bio = (String)data.get("bio");

                admin.setId(id);
                admin.setName(name);
                admin.setSurname(surname);
                admin.setPatronymic(patronymic);
                admin.setPhone(phone);
                admin.setBio(bio);

                admins.add(admin);
            }
            return admins;
        }
        catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public static AdminInfo getByDate(String token, LocalDate date){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/shedule/admin?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            AdminInfo admin = null;

            Long adminId = (Long) data.get("admin_id");
            if(adminId != -1){
                admin = getById(token, adminId);
            }
            else{
                admin = new AdminInfo();
                admin.setName("");
                admin.setSurname("Не назначен");
            }

            return admin;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static AdminInfo getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/users/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            AdminInfo admin = new AdminInfo();

            String name = (String)data.get("name");
            String surname = (String)data.get("surname");
            String patronymic = (String)data.get("patronymic");
            String phone = (String)data.get("phone");
            String bio = (String)data.get("bio");

            admin.setId(id);
            admin.setName(name);
            admin.setSurname(surname);
            admin.setPatronymic(patronymic);
            admin.setPhone(phone);
            admin.setBio(bio);

            return admin;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }
}
