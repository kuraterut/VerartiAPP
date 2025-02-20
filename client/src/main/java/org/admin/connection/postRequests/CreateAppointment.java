package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Appointment;
import org.admin.utils.Response;
import org.admin.utils.entities.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateAppointment extends Connection {
    public static Response post(String token, Appointment appointment){
        try{
            getConnection("http://localhost:8000/api/admin/schedule/");
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject outJson = new JSONObject();


            LocalDate date = appointment.getDate();
            String dateStr = date.getYear()+"-";
            dateStr += date.getMonthValue()+"-";
            dateStr += date.getDayOfMonth();

            LocalTime startTime = appointment.getStartTime();
            String startTimeStr = startTime.getHour()+":"+startTime.getMinute();

            JSONArray servicesJSON = new JSONArray();
            for(Service service : appointment.getServices()){
                servicesJSON.add(service.getId());
            }

            Long clientId = appointment.getClient().getId();
            Long masterId = appointment.getMaster().getId();
            String comment = appointment.getComment();

            outJson.put("date", dateStr);
            outJson.put("start_time", startTimeStr);
            outJson.put("services", servicesJSON);
            outJson.put("client_id", clientId);
            outJson.put("master_id", masterId);
            outJson.put("comment", comment);

            sendJson(outJson);

            Response response = new Response();
            response.setCode(connection.getResponseCode());

            return response;

        }
        catch(Exception ex){
            System.out.println(ex);

            Response response = new Response();
            return response;
        }
    }
}
