package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Master;
import org.admin.utils.Response;
import org.admin.utils.entities.Option;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UpdateMaster extends Connection {
    public static Response updateInfo(String token, Master master){
        try {
            getConnection("http://localhost:8000/api/admin/users/master/" + master.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = new JSONObject();
            out.put("name", master.getName());
            out.put("surname", master.getSurname());
            out.put("patronymic", master.getPatronymic());
            out.put("phone", master.getPhone());
            out.put("bio", master.getBio());
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception e) {
            System.out.println(e);
            return new Response();
        }
    }
}
