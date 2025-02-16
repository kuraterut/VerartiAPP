package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.ServiceInfo;
import org.json.simple.JSONObject;

import java.time.LocalTime;

public class GetService extends Connection {
    public static ServiceInfo getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/service/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            ServiceInfo service = new ServiceInfo();

            String name = (String)data.get("name");
            String description = (String)data.get("description");
            Double price = (Double)data.get("price");
            String[] timeStr = ((String)data.get("date")).split(":");
            LocalTime time = LocalTime.of(Integer.valueOf(timeStr[0]), Integer.valueOf(timeStr[1]));

            service.setId(id);
            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);
            service.setTime(time);

            return service;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }
}
