package org.admin.connection.deleteRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;

public class DeleteOption extends Connection {
    public static Response deleteByMasterId(String token, Long option_id, Long master_id) {
        try {
            getConnection("http://localhost:8000/api/admin/option/master?option_id="+option_id+"?master_id="+master_id);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: DeleteOption, method: deleteByMasterId, exception: " + ex.getMessage());
            return new Response();
        }
    }

    public static Response deleteById(String token, Long id){
        try {
            getConnection("http://localhost:8000/api/admin/option/" + id);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: DeleteOption, method: deleteById, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
