package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PutAdminOnDate extends Connection {
    public static Response post(String token, Long adminId, LocalDate date){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/admin");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = date.format(formatter);

            JSONObject outJson = new JSONObject();
            outJson.put("date", dateStr);
            outJson.put("admin_id", adminId);

            sendJson(outJson);
            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }

            return new Response(status, getErrorMsg());
        }
        catch (Exception ex){
            System.out.println("class: PutAdminOnDate, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
