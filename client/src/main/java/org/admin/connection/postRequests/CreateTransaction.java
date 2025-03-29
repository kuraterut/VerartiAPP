package org.admin.connection.postRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.model.Response;
import org.admin.model.Transaction;
import org.admin.utils.TransactionType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class CreateTransaction extends Connection {
    public static Response post(String token, List<Transaction> transactions){
        try{
            getConnection("http://localhost:8000/api/admin/transaction/");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            JSONObject transactionsJSONObject = new JSONObject();
            JSONArray transactionsJSONArray = new JSONArray();
            for(Transaction transaction : transactions){
                TransactionType type = transaction.getType();
                Long amount = transaction.getAmount();
                Integer count = transaction.getCount();
                String timestamp = HelpFuncs.localDateTimeToString(transaction.getTimestamp(), "yyyy-MM-dd HH:mm:ss");
                Long unitId = transaction.getUnitId();

                JSONObject transactionJSONObject = new JSONObject();
                transactionJSONObject.put("type", type.toString());
                transactionJSONObject.put("amount", amount);
                transactionJSONObject.put("count", count);
                transactionJSONObject.put("timestamp", timestamp);
                transactionJSONObject.put("unit_id", unitId);

                transactionsJSONArray.add(transactionJSONObject);
            }
            transactionsJSONObject.put("transactions", transactionsJSONArray);

            sendJson(transactionsJSONObject);

            int status = connection.getResponseCode();
            if(status == 200){
                return new Response(200, "");
            }
            return new Response(status, getErrorMsg());
        }
        catch(Exception ex){
            System.out.println("class: CreateTransaction, method: post, exception: " + ex.getMessage());
            return new Response();
        }
    }
}
