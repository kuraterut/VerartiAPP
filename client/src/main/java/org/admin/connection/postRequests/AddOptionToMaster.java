package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class AddOptionToMaster extends Connection {
    public static Response post(String token, Long masterId, Long service_id){
        try {
            getConnection("http://localhost:8000/api/admin/option/"+service_id);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = new JSONObject();
            out.put("master_id", masterId);
            sendJson(out);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "OK");
            }

            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: AddOptionToMaster, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
