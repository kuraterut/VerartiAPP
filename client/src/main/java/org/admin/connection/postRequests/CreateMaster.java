package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.entities.Admin;
import org.admin.utils.entities.Master;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateMaster extends Connection {
    public static Response post(String token, Master master){
        try{
            getConnection("http://localhost:8000/api/admin/users/signup");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            String name = master.getName();
            String surname = master.getSurname();
            String patronymic = master.getPatronymic();
            String phone = master.getPhone();
            String email = master.getEmail();
            String password = master.getPassword();

            ArrayList<String> roles = new ArrayList<>();
            roles.add("master");
            if(master.getIsAdmin()) roles.add("admin");

            JSONObject outJson = new JSONObject();
            outJson.put("name", name);
            outJson.put("surname", surname);
            outJson.put("patronymic", patronymic);
            outJson.put("phone", phone);
            outJson.put("email", email);
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
            System.out.println(ex);
            return new Response();
        }
    }
}
