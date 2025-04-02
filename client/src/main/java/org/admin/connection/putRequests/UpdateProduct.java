package org.admin.connection.putRequests;

import org.admin.connection.Connection;
import org.admin.model.Option;
import org.admin.model.Product;
import org.admin.model.Response;
import org.json.simple.JSONObject;

public class UpdateProduct extends Connection {
    public static Response updateInfo(String token, Product product){
        try {
            getConnection("http://localhost:8000/api/admin/product/" + product.getId());
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject out = product.toJson();

            System.out.println(out.toJSONString());
            sendJson(out);

            int status = connection.getResponseCode();
            if(status != 200) {
                return new Response(status, getErrorMsg());
            }
            return new Response(200, "OK");
        }
        catch (Exception ex) {
            System.out.println("class: UpdateProduct, method: updateInfo, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
