package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.entities.Service;
import org.json.simple.JSONObject;

public class CreateService extends Connection {
    public static Response post(String token, Service service){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            String name = service.getName();
            Long price = service.getPrice();
            String duration = service.getDurationString();
            String description = service.getDescription();


            JSONObject outJson = new JSONObject();
            outJson.put("name", name);
            outJson.put("price", price.intValue());
            outJson.put("duration", duration);
            outJson.put("description", description);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());

        }
        catch(Exception ex){
            System.out.println(ex);
            return new Response();
        }
    }
}
