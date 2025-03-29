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
                Product product = new Product();

                Long id = (Long) productJSON.get("id");
                String name = (String)productJSON.get("name");
                String description = (String)productJSON.get("description");
                Long price = (Long) productJSON.get("price");
                Integer count = (Integer) productJSON.get("count");

                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setCount(count);

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
            Product product = new Product();

            String name = (String)data.get("name");
            String description = (String) data.get("description");
            Long price = (Long) data.get("price");
            Integer count = (Integer) data.get("count");

            product.setId(id);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCount(count);

            return product;
        }
        catch (Exception ex){
            System.out.println("class: GetProduct, method: getById, exception: " + ex.getMessage());
            return null;
        }
    }
}
