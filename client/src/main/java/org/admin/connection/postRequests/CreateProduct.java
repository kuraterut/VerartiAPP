package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.model.Response;
import org.admin.model.Product;
import org.json.simple.JSONObject;

public class CreateProduct extends Connection {
    public static Response post(String token, Product product){
        try{
            getConnection("http://localhost:8000/api/admin/product/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);


            String name = product.getName();
            String description = product.getDescription();
            Long price = product.getPrice();
            Integer count = product.getCount();

            JSONObject outJson = new JSONObject();
            outJson.put("name", name);
            outJson.put("description", description);
            outJson.put("price", price);
            outJson.put("count", count);

            sendJson(outJson);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: CreateProduct, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
