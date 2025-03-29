package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.Appointment;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetAppointment extends Connection {
    public static List<Appointment> getListByClientId(String token, Long clientId){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/clients/" + clientId);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<Appointment> list = new ArrayList<>();

            JSONObject data = getJson();

            JSONArray jsonArr = (JSONArray)data.get("appointments");
            for(Object elem : jsonArr){
                JSONObject obj = (JSONObject)elem;
                Appointment appointment = Appointment.fromJson(obj);
                list.add(appointment);
            }

            return list;
        }
        catch(Exception ex){
            System.out.println("class: GetAppointment, method: getListByClientId, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Appointment getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            return Appointment.fromJson(data);

        }
        catch(Exception ex){
            System.out.println("class: GetAppointment, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }

    public static List<Appointment> getListByDate(String token, LocalDate date){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/date?date=" + HelpFuncs.localDateToString(date, "yyyy-MM-dd"));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            List<Appointment> appointments = new ArrayList<>();

            JSONArray jsonArr = (JSONArray)data.get("appointments");
            for(Object elem : jsonArr){
                JSONObject obj = (JSONObject)elem;
                Appointment appointment = Appointment.fromJson(obj);
                appointments.add(appointment);
            }
            return appointments;
        }
        catch(Exception ex){
            System.out.println("class: GetAppointment, method: getListByDate, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
