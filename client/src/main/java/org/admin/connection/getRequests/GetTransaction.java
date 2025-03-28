package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.utils.entities.Admin;
import org.admin.utils.entities.Transaction;
import org.admin.utils.entities.TransactionType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetTransaction extends Connection {
    public static List<Transaction> getByDate(String token, LocalDate date){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/transaction/day?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            List<Transaction> transactions = new ArrayList<>();
            JSONArray transactionsJSON = (JSONArray) data.get("transactions");
            for(Object transactionObj : transactionsJSON){
                JSONObject transactionJSON = (JSONObject) transactionObj;
                Transaction transaction = new Transaction();

                Long id = (Long) transactionJSON.get("id");
                TransactionType type = TransactionType.valueOf((String)transactionJSON.get("type"));
                Long amount = (Long)transactionJSON.get("amount");
                Integer count = (Integer) transactionJSON.get("count");
                LocalDateTime timestamp = LocalDateTime.parse((String)transactionJSON.get("timestamp"));
                Long unitId = (Long)transactionJSON.get("unit_id");

                transaction.setId(id);
                transaction.setType(type);
                transaction.setAmount(amount);
                transaction.setCount(count);
                transaction.setTimestamp(timestamp);
                transaction.setUnitId(unitId);
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
