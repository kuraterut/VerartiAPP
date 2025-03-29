package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetProduct extends Connection {
    public static List<Product> getAll(String token){
        try{
            getConnection("http://localhost:8000/api/admin/product/");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            List<Product> products = new ArrayList<>();
            JSONArray productsJSON = (JSONArray) data.get("products");
            for(Object productObj : productsJSON){
                JSONObject productJSON = (JSONObject) productObj;
                Product product = Product.fromJson(productJSON);
                products.add(product);
            }
            return products;
        }
        catch (Exception ex){
            System.out.println("class: GetProduct, method: getAll, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static Product getById(String token, Long id){
        try{
            getConnection("http://localhost:8000/api/admin/product/" + id);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            return Product.fromJson(data);
        }
        catch (Exception ex){
            System.out.println("class: GetProduct, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }
}
