package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetClient extends Connection {
    public static List<Client> getAll(String token){
        try{
            //TODO Всегда возвращать с кодом(Юзать наследование от Response)
            getConnection("http://localhost:8000/api/admin/clients/");

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<Client> clients = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("clients");

            for(Object elemObj : jsonArr) {
                JSONObject elem = (JSONObject)elemObj;
                Client client = Client.fromJson(elem);
                clients.add(client);
            }

            return clients;
        }
        catch(Exception ex){
            System.out.println("class: GetClient, method: getAll, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Client getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/clients/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            return Client.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetClient, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }

    public static Client getByPhone(String token, String phone){
        try{
            getConnection("http://localhost:8000/api/admin/clients/phone?phone=%2B"+phone.substring(1));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            return Client.fromJson(data);
        }
        catch(Exception ex){
            System.out.println("class: GetClient, method: getByPhone, exception: " + ex.getMessage());
            return null;
        }
    }
}