package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.Master;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateMaster extends Connection {
    public static Response post(String token, Master master){
        try{
            getConnection("http://localhost:8000/api/admin/users/signup");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = master.toJson();
            ArrayList<String> roles = new ArrayList<>();
            roles.add("MASTER");
            if(master.getIsAdmin()) roles.add("ADMIN");
            outJson.put("roles", roles);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: CreateMaster, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
