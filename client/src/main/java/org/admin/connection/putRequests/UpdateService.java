package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.entities.Service;
import org.json.simple.JSONObject;

public class UpdateService extends Connection {
    public static Response updateInfo(String token, Service service){
        try {
            getConnection("http://localhost:8000/api/admin/option/" + service.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject out = new JSONObject();
            out.put("name", service.getName());
            out.put("price", service.getPrice());
            out.put("duration", service.getDurationString());
            out.put("description", service.getDescription());

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
