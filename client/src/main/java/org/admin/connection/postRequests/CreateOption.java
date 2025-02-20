package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.entities.Option;
import org.json.simple.JSONObject;

public class CreateOption extends Connection {
    public static Response post(String token, Option option){
        try{
            getConnection("http://localhost:8000/api/admin/option/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            String name = option.getName();
            Long price = option.getPrice();
            String duration = option.getDurationString();
            String description = option.getDescription();


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
