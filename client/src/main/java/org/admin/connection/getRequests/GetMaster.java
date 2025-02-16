package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.Appointment;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMaster extends Connection {
    public static List<MasterInfo> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/users/master");

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<MasterInfo> masters = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("masters");

            for(Object elem: jsonArr){
                JSONObject masterJSON = (JSONObject) elem;
                MasterInfo master = new MasterInfo();

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

    public static MasterInfo getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/master/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            MasterInfo master = new MasterInfo();

            String name = (String)data.get("name");
            String surname = (String)data.get("surname");
            String patronymic = (String)data.get("patronymic");
            String bio = (String)data.get("bio");
            String phone = (String)data.get("phone");

            List<ServiceInfo> services = new ArrayList<>();
            JSONArray servicesArr = (JSONArray)data.get("services_id");
            for(Object elem: servicesArr){
                Long serviceId = (Long)elem;
                ServiceInfo service = GetService.getById(token, serviceId);
                services.add(service);
            }


            master.setId(id);
            master.setName(name);
            master.setSurname(surname);
            master.setPatronymic(patronymic);
            master.setBio(bio);
            master.setPhone(phone);
            master.setServices(services);

            return master;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static List<MasterInfo> getListByDate(String token, LocalDate date){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = formatter.format(date);
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/schedule/master?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<MasterInfo> masters = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray mastersArr = (JSONArray)data.get("masters");
            for(Object elem: mastersArr){
                JSONObject masterJSON = (JSONObject)elem;
                MasterInfo master = new MasterInfo();
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
