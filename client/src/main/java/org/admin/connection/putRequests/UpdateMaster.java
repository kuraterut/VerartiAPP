package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Master;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class UpdateMaster extends Connection {
    public static Response updateInfo(String token, Master master){
        try {
            getConnection("http://localhost:8000/api/admin/users/master/" + master.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = master.toJson();
            out.remove("password");
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateMaster, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
