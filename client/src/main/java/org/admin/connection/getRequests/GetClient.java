package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.entities.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetClient extends Connection {
    public static List<Client> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/clients/");

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<Client> clients = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("clients");

            for(Object elemObj : jsonArr) {
                JSONObject elem = (JSONObject)elemObj;
                Client client = new Client();

                Long id = (Long)elem.get("id");
                String name = (String)elem.get("name");
                String surname = (String)elem.get("surname");
                String patronymic = (String)elem.get("patronymic");
                String phone = (String)elem.get("phone");
                String comment = (String)elem.get("comment");
                String[] birthdayStr = ((String)elem.get("birthday")).split("-");
                LocalDate birthday = LocalDate.of(Integer.valueOf(birthdayStr[0]), Integer.valueOf(birthdayStr[1]), Integer.valueOf(birthdayStr[2]));

                client.setId(id);
                client.setName(name);
                client.setSurname(surname);
                client.setPatronymic(patronymic);
                client.setPhone(phone);
                client.setComment(comment);
                client.setBirthday(birthday);

                clients.add(client);
            }

            return clients;
        }
        catch(Exception ex){
            System.out.println(ex);
            return new ArrayList<>();
        }
    }

    public static Client getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/clients/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();

            Client client = new Client();

            String name = (String)data.get("name");
            String surname = (String)data.get("surname");
            String patronymic = (String)data.get("patronymic");
            String phone = (String)data.get("phone");
            String comment = (String)data.get("comment");
            String[] birthdayStr = ((String)data.get("birthday")).split("-");
            LocalDate birthday = LocalDate.of(Integer.valueOf(birthdayStr[0]), Integer.valueOf(birthdayStr[1]), Integer.valueOf(birthdayStr[2]));


            client.setId(id);
            client.setName(name);
            client.setSurname(surname);
            client.setPatronymic(patronymic);
            client.setPhone(phone);
            client.setComment(comment);
            client.setBirthday(birthday);

            return client;

        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static Client getByPhone(String token, String phone){
        try{
            getConnection("http://localhost:8000/api/admin/clients/phone?phone=%2B"+phone.substring(1));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            if (connection.getResponseCode() == 200){
                Client client = new Client();

                Long id = (Long)data.get("id");
                String name = (String)data.get("name");
                String surname = (String)data.get("surname");
                String patronymic = (String)data.get("patronymic");
                // String phone = (String)data.get("phone");
                String comment = (String)data.get("comment");
                String[] birthdayStr = ((String)data.get("birthday")).split("-");
                LocalDate birthday = LocalDate.of(Integer.valueOf(birthdayStr[0]), Integer.valueOf(birthdayStr[1]), Integer.valueOf(birthdayStr[2]));

                client.setCode(200);
                client.setId(id);
                client.setName(name);
                client.setSurname(surname);
                client.setPatronymic(patronymic);
                client.setPhone(phone);
                client.setComment(comment);
                client.setBirthday(birthday);

                return client;
            }
            else{
                Client client = new Client();
                client.setCode(connection.getResponseCode());
                client.setMsg(connection.getResponseMessage());

                return client;
            }
        }
        catch(Exception ex){
            System.out.println(ex);
            Client client = new Client();
            client.setCode(500);
            client.setMsg(ex.getMessage());
            return client;
        }
    }
}