package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Client;
import org.admin.utils.HelpFuncs;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class UpdateClient extends Connection {
    public static Response updateInfo(String token, Client client){
        try {
            getConnection("http://localhost:8000/api/admin/clients/" + client.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = client.toJson();
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateClient, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
