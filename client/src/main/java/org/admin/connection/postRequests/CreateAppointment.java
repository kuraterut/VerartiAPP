package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.model.Appointment;
import org.admin.model.Response;
import org.admin.model.Option;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateAppointment extends Connection {
    public static Response post(String token, Appointment appointment){
        try{
            //TODO Комментарий к записи
            getConnection("http://localhost:8000/api/admin/appointment/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = new JSONObject();

            LocalDate date = appointment.getDate();
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");

            LocalTime startTime = appointment.getStartTime();
            String startTimeStr = HelpFuncs.localTimeToString(startTime, "HH:mm");

            JSONArray optionsJSON = new JSONArray();
            for(Option option : appointment.getOptions()){
                optionsJSON.add(option.getId());
            }

            Long clientId = appointment.getClient().getId();
            Long masterId = appointment.getMaster().getId();
            String comment = appointment.getComment();

            outJson.put("date", dateStr);
            outJson.put("start_time", startTimeStr);
            outJson.put("option_ids", optionsJSON);
            outJson.put("client_id", clientId);
            outJson.put("master_id", masterId);
            outJson.put("comment", comment);

            sendJson(outJson);

            Response response = new Response();
            response.setCode(connection.getResponseCode());

            return response;

        }
        catch(Exception ex){
            System.out.println("class: CreateAppointment, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
