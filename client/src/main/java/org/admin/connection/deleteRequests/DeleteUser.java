package org.admin.connection.deleteRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.utils.HelpFuncs;

import java.time.LocalDate;

public class DeleteUser extends Connection {
    public static Response deleteById(String token, Long id){
        try {
            getConnection("http://localhost:8000/api/admin/users/" + id);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: DeleteUser, method: deleteById, exception: " + ex.getMessage());
            return new Response();
        }
    }

    public static Response removeMasterFromDate(String token, Long master_id, LocalDate date){
        try {
            getConnection("http://localhost:8000/api/admin/appointment/master?master_id="+master_id+"&date="+ HelpFuncs.localDateToString(date, "yyyy-MM-dd"));
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: DeleteUser, method: deleteById, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
