package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.admin.utils.entities.Option;
import org.json.simple.JSONObject;

public class UpdateOption extends Connection {
    public static Response updateInfo(String token, Option option){
        try {
            getConnection("http://localhost:8000/api/admin/option/" + option.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject out = new JSONObject();
            out.put("name", option.getName());
            out.put("price", option.getPrice());
            out.put("duration", option.getDurationString());
            out.put("description", option.getDescription());

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
