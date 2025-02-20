package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GetService extends Connection {
    public static List<Service> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/option/");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            List<Service> services = new ArrayList<>();
            JSONArray servicesArray = (JSONArray) data.get("options");
            for (Object serviceObj : servicesArray) {
                JSONObject serviceJSON = (JSONObject) serviceObj;
                Service service = new Service();

                Long id = (Long) serviceJSON.get("id");
                String name = (String) serviceJSON.get("name");
                String description = (String) serviceJSON.get("description");
                Long price = (Long) serviceJSON.get("price");
                String[] durationStr = ((String)serviceJSON.get("duration")).split(":");
                LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

                service.setId(id);
                service.setName(name);
                service.setDescription(description);
                service.setPrice(price);
                service.setDuration(duration);

                services.add(service);
            }
            return services;

        }catch (Exception e){
            System.out.println("class GetService, getAll " + e);
            return new ArrayList<>();
        }
    }

    public static Service getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/option/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();


            Service service = new Service();
            String name = (String)data.get("name");
            String description = (String)data.get("description");
            Long price = (Long)data.get("price");
            String[] durationStr = ((String)data.get("duration")).split(":");
            LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

            service.setId(id);
            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);
            service.setDuration(duration);

            return service;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }
    public static List<Service> getListByMasterId(String token, Long masterId){
        try{
            getConnection("http://localhost:8000/api/admin/option/master/" + masterId);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            List<Service> services = new ArrayList<>();
            JSONArray servicesArray = (JSONArray) data.get("options");
            for (Object serviceObj : servicesArray) {
                JSONObject serviceJSON = (JSONObject) serviceObj;
                Service service = new Service();

                Long id = (Long) serviceJSON.get("id");
                String name = (String) serviceJSON.get("name");
                String description = (String) serviceJSON.get("description");
                Long price = (Long) serviceJSON.get("price");
                String[] durationStr = ((String)data.get("duration")).split(":");
                LocalTime duration = LocalTime.of(Integer.parseInt(durationStr[0]), Integer.parseInt(durationStr[1]));

                service.setId(id);
                service.setName(name);
                service.setDescription(description);
                service.setPrice(price);
                service.setDuration(duration);

                services.add(service);
            }
            return services;

        }
        catch(Exception ex){
            System.out.println("class GetService, getListByMasterId " + ex);
            return new ArrayList<>();
        }
    }
}
