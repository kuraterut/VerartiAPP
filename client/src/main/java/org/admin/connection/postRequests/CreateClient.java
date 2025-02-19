package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.ClientInfo;
import org.admin.utils.HelpFuncs;
import org.admin.utils.Response;
import org.json.simple.JSONObject;

import java.time.LocalDate;

public class CreateClient extends Connection {
    public static Response post(String token, ClientInfo client){
        try{
            getConnection("http://localhost:8000/api/admin/clients/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            String name = client.getName();
            String surname = client.getSurname();
            String patronymic = client.getPatronymic();
            String phone = client.getPhone();
            LocalDate birthday = client.getBirthday();

            String birthdayStr = HelpFuncs.localDateToString(birthday, "yyyy-MM-dd");
            JSONObject outJson = new JSONObject();
			outJson.put("birthday", birthdayStr);
            outJson.put("name", name);
            outJson.put("surname", surname);
            outJson.put("patronymic", patronymic);
            outJson.put("phone", phone);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());

        }
        catch(Exception ex){
            System.out.println(ex);
            return new Response();
        }
    }
}
