package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.Response;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PutMasterOnDate extends Connection {
    public static Response post(String token, Long masterId, LocalDate date){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/master");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = date.format(formatter);

            JSONObject outJson = new JSONObject();
            outJson.put("date", dateStr);
            outJson.put("master_id", masterId);

            sendJson(outJson);
            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }

            return new Response(status, getErrorMsg());
        }
        catch (Exception ex){
            System.out.println(ex);
            return new Response();
        }
    }
}
