package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Master;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                Master master = new Master();

                Long id = (Long) masterJSON.get("id");
                String name = (String) masterJSON.get("name");
                String surname = (String) masterJSON.get("surname");
                String patronymic = (String) masterJSON.get("patronymic");
                String phone = (String) masterJSON.get("phone");
                String bio = (String) masterJSON.get("bio");

                master.setId(id);
                master.setName(name);
                master.setSurname(surname);
                master.setPatronymic(patronymic);
                master.setPhone(phone);
                master.setBio(bio);

                masters.add(master);
            }
            return masters;


        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static Master getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/users/master/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            Master master = new Master();

            String name = (String)data.get("name");
            String surname = (String)data.get("surname");
            String patronymic = (String)data.get("patronymic");
            String bio = (String)data.get("bio");
            String phone = (String)data.get("phone");

            master.setId(id);
            master.setName(name);
            master.setSurname(surname);
            master.setPatronymic(patronymic);
            master.setBio(bio);
            master.setPhone(phone);
            master.setServices(new ArrayList<>());

            return master;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static List<Master> getListByDate(String token, LocalDate date, boolean appointed){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = formatter.format(date);
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            String url = "http://localhost:8000/api/admin/schedule/master?";
            url += "date=" + encodedDate + "&appointed=" + appointed;
            getConnection(url);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<Master> masters = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray mastersArr = (JSONArray)data.get("masters");
            for(Object elem: mastersArr){
                JSONObject masterJSON = (JSONObject)elem;
                Master master = new Master();
                Long id = (Long)masterJSON.get("id");
                String name = (String)masterJSON.get("name");
                String surname = (String)masterJSON.get("surname");
                String patronymic = (String)masterJSON.get("patronymic");
                String phone = (String)masterJSON.get("phone");
                String bio = (String)masterJSON.get("bio");

                master.setId(id);
                master.setName(name);
                master.setSurname(surname);
                master.setPatronymic(patronymic);
                master.setPhone(phone);
                master.setBio(bio);

                masters.add(master);
            }

            return masters;

        }
        catch(Exception ex){
            System.out.println(ex);
            return new ArrayList<>();
        }
    }
}
