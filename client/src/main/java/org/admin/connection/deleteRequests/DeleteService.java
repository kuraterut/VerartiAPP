package org.admin.connection.deleteRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.json.simple.JSONObject;

public class DeleteService extends Connection {
    public static Response deleteByMasterId(String token, Long service_id, Long master_id) {
        try {
            getConnection("http://localhost:8000/api/admin/appointment/");
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject out = new JSONObject();
            out.put("option_id", service_id);
            out.put("master_id", master_id);

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
