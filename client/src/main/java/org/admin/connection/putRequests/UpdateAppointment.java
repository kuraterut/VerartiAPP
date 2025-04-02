package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Appointment;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class UpdateAppointment extends Connection {
    public static Response updateInfo(String token, Appointment appointment){
        try {
            getConnection("http://localhost:8000/api/admin/appointment/" + appointment.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = appointment.toJson();
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateAppointment, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
