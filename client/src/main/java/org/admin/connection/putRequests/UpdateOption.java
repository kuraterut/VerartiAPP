package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.Option;
import org.json.simple.JSONObject;

public class UpdateOption extends Connection {
    public static Response updateInfo(String token, Option option){
        try {
            getConnection("http://localhost:8000/api/admin/option/" + option.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = option.toJson();
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateOption, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
