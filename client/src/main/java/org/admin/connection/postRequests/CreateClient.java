package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Client;
import org.admin.utils.HelpFuncs;
import org.admin.model.Response;
import org.json.simple.JSONObject;

import java.time.LocalDate;

public class CreateClient extends Connection {
    public static Response post(String token, Client client){
        try{
            getConnection("http://localhost:8000/api/admin/clients/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = client.toJson();
            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());

        }
        catch(Exception ex){
            System.out.println("class: CreateClient, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
