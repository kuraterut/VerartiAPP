package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Admin;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class UpdateAdmin extends Connection {
    public static Response updateInfo(String token, Admin admin){
        try {
            getConnection("http://localhost:8000/api/admin/users/admin/" + admin.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = new JSONObject();
            out.put("name", admin.getName());
            out.put("surname", admin.getSurname());
            out.put("patronymic", admin.getPatronymic());
            out.put("phone", admin.getPhone());
            out.put("bio", admin.getBio());
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateAdmin, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
