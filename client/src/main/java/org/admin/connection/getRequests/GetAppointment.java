package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Appointment;
import org.admin.utils.entities.Client;
import org.admin.utils.entities.Master;
import org.admin.utils.entities.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GetAppointment extends Connection {
    public static List<Appointment> getListByClientId(String token, Long clientId){
        try{
            getConnection("http://localhost:8000/api/admin/shedule/clients/" + clientId);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            List<Appointment> list = new ArrayList<>();

            JSONObject data = getJson();

            JSONArray jsonArr = (JSONArray)data.get("shedules");
            for(Object elem : jsonArr){
                JSONObject obj = (JSONObject)elem;

                Long appointmentId = (Long)obj.get("id");
                Appointment appointment = getById(token, appointmentId);
                // String appointmentStatus = (String)obj.get("status");
                // String[] appointmentStartTimeStr = ((String)obj.get("start_time")).split(":");
                // LocalTime appointmentStartTime 	= LocalTime.of(Integer.valueOf(startTimeStr[0]), Integer.valueOf(startTimeStr[1]));
                // String[] appointmentDateStr = ((String)data.get("date")).split("-");
                // LocalDate appointmentDate = LocalDate.of(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]), Integer.valueOf(dateStr[2]));

                // JSONObject clientJSON = (JSONObject)obj.get("client");
                // JSONObject masterJSON = (JSONObject)obj.get("master");
                // JSONObject serviceJSON = (JSONObject)obj.get("appointment");

                // ClientInfo client = new ClientInfo();
                // MasterInfo master = new MasterInfo();
                // ServiceInfo service = new ServiceInfo();

                // client.setId((Long)clientJSON.get("id"));
                // client.setName((String)clientJSON.get("name"));
                // client.setSurname((String)clientJSON.get("surname"));
                // client.setPatronymic((String)clientJSON.get("patronymic"));
                // client.setEmail((String)clientJSON.get("email"));
                // client.setPhone((String)clientJSON.get("phone"));
                // client.setComment((String)clientJSON.get("comment"));
                // client.setBirthdayStr((String)clientJSON.get("birthday"));

                // master.setId((Long)masterJSON.get("id"));
                // master.setName((String)masterJSON.get("name"));
                // master.setSurname((String)masterJSON.get("surname"));
                // master.setPatronymic((String)masterJSON.get("patronymic"));

                list.add(appointment);
            }

            return list;
        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static Appointment getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/appointment/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            Appointment appointment = new Appointment();

            List<Service> services = new ArrayList<>();

            Long masterId 			= (Long)data.get("master_id");
            Long clientId 			= (Long)data.get("client_id");
            String comment 			= (String)data.get("comment");
            String[] startTimeStr 	= ((String)data.get("start_time")).split(":");
            LocalTime startTime 	= LocalTime.of(Integer.valueOf(startTimeStr[0]), Integer.valueOf(startTimeStr[1]));
            String[] dateStr = ((String)data.get("date")).split("-");
            LocalDate date = LocalDate.of(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]), Integer.valueOf(dateStr[2]));

            JSONArray servicesArr = (JSONArray)data.get("services");
            for(Object serviceObj: servicesArr){
                Long serviceId = (Long)serviceObj;
                Service service = GetService.getById(token, serviceId);
                services.add(service);
            }

            Master master = GetMaster.getById(token, masterId);
            Client client = GetClient.getById(token, clientId);

            appointment.setId(id);
            appointment.setStartTime(startTime);
            appointment.setClient(client);
            appointment.setMaster(master);
            appointment.setDate(date);
            appointment.setServices(services);
            appointment.setComment(comment);

            return appointment;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }
}
