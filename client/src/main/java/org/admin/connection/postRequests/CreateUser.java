package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateUser extends Connection {
    public static Response post(String token, User user){
        try{
            getConnection("http://localhost:8000/api/admin/users/signup");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            String name = user.getName();
            String surname = user.getSurname();
            String patronymic = user.getPatronymic();
            String phone = user.getPhone();
            String email = user.getEmail();
            String password = user.getPassword();
            ArrayList<String> roles = user.getRoles();

            JSONObject outJson = new JSONObject();
            outJson.put("name", name);
            outJson.put("surname", surname);
            outJson.put("patronymic", patronymic);
            outJson.put("phone", phone);
            outJson.put("email", email);
            outJson.put("password", password);
            outJson.put("roles", roles);
            System.out.println(outJson);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, "Ошибка");
        }
        catch(Exception ex){
            System.out.println(ex);
            return new Response(404, "Ошибка подключения к серверу");
        }
    }
}
