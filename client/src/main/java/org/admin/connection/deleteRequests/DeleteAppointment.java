package org.admin.connection.deleteRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;

public class DeleteAppointment extends Connection {
    public static Response deleteById(String token, Long id){
        try {
            getConnection("http://localhost:8000/api/admin/appointment/" + id);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: DeleteAppointment, method: deleteById, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
