package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Admin;
import org.admin.model.Response;
import org.json.simple.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class UpdateProfile extends Connection {
    public static Response updateInfo(String token, Admin admin){
        try {
            getConnection("http://localhost:8000/api/admin/profile/info");
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            //TODO Убрать Update для Мастеров и Админов в EnterpriseWindow
            JSONObject out = new JSONObject();
            out.put("name", admin.getName());
            out.put("surname", admin.getSurname());
            out.put("patronymic", admin.getPatronymic());
            out.put("bio", admin.getBio());
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateProfile, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }

    public static Response updatePassword(String token, String oldPassword, String newPassword){
        try {
            getConnection("http://localhost:8000/api/admin/profile/password");
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = new JSONObject();
            out.put("old_password", oldPassword);
            out.put("new_password", newPassword);
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateProfile, method: updatePassword, exception: " + ex.getMessage());
            return new Response();
        }
    }

    public static Response updateProfilePhoto(String token, File file){
        try{
            getConnection("http://localhost:8000/api/admin/profile/photo");

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject outJson = new JSONObject();
            byte[] byteArray = Files.readAllBytes(file.toPath());
            String encodedPhoto = Base64.getEncoder().encodeToString(byteArray);
            outJson.put("photo", encodedPhoto);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200) return new Response(200, "");
            else return new Response(status, "Ошибка");

        }
        catch(Exception ex){
            System.out.println("class: UpdateProfile, method: updateProfilePhoto, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
