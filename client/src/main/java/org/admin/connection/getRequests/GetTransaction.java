package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.model.Transaction;
import org.admin.utils.TransactionType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetTransaction extends Connection {
    public static List<Transaction> getByDate(String token, LocalDate date){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/transaction/date?date=" + encodedDate);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            JSONObject data = getJson();
            List<Transaction> transactions = new ArrayList<>();
            JSONArray transactionsJSON = (JSONArray) data.get("transactions");
            for(Object transactionObj : transactionsJSON){
                JSONObject transactionJSON = (JSONObject) transactionObj;
                Transaction transaction = Transaction.fromJson(transactionJSON);
                transactions.add(transaction);
            }
            return transactions;
        }
        catch (Exception ex){
            System.out.println("class: GetTransaction, method: getByDate, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
