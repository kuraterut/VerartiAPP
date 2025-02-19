package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.utils.AdminInfo;
import org.admin.utils.ClientInfo;
import org.admin.utils.HelpFuncs;
import org.admin.utils.Response;
import org.json.simple.JSONObject;

public class UpdateClient extends Connection {
    public static Response updateInfo(String token, ClientInfo client){
        try {
            getConnection("http://localhost:8000/api/admin/clients/" + client.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject out = new JSONObject();
            out.put("name", client.getName());
            out.put("surname", client.getSurname());
            out.put("patronymic", client.getPatronymic());
            out.put("phone", client.getPhone());
            out.put("birthday", HelpFuncs.localDateToString(client.getBirthday(), "yyyy-MM-dd"));
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
