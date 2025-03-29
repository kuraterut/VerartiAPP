package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.Admin;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateAdmin extends Connection {
    public static Response post(String token, Admin admin){
        try{
            getConnection("http://localhost:8000/api/admin/users/signup");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            String name = admin.getName();
            String surname = admin.getSurname();
            String patronymic = admin.getPatronymic();
            String phone = admin.getPhone();
            String password = admin.getPassword();

            ArrayList<String> roles = new ArrayList<>();
            roles.add("ADMIN");
            if(admin.getIsMaster()) roles.add("MASTER");

            JSONObject outJson = new JSONObject();
            outJson.put("name", name);
            outJson.put("surname", surname);
            outJson.put("patronymic", patronymic);
            outJson.put("phone", phone);
            outJson.put("password", password);
            outJson.put("roles", roles);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: CreateAdmin, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
