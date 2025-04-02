package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.User;
import org.json.simple.JSONObject;

public class CreateUser extends Connection {
    public static Response post(String token, User admin){
        try{
            getConnection("http://localhost:8000/api/admin/users/signup");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            JSONObject outJson = admin.toJson();
            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: CreateUser, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
