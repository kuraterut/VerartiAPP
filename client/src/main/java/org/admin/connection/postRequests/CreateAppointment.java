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
            getConnection("http://localhost:8000/api/admin/appointment/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = appointment.toJson();
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
