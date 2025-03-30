package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.Option;
import org.json.simple.JSONObject;

public class CreateOption extends Connection {
    public static Response post(String token, Option option){
        try{
            getConnection("http://localhost:8000/api/admin/option/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = option.toJson();

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());

        }
        catch(Exception ex){
            System.out.println("class: CreateOption, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
