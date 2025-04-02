package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.utils.HelpFuncs;
import org.admin.model.Transaction;
import org.admin.utils.PaymentMethod;
import org.admin.utils.TransactionType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetTransaction extends Connection {
    public static List<Transaction> getListByDate(String token, LocalDate date){
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
            System.out.println("class: GetTransaction, method: getListByDate, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Transaction> getListByDate(String token, LocalDate date, TransactionType transactionType){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/transaction/type?date=" + encodedDate + "&transaction_type=" + transactionType.toString());
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
            System.out.println("class: GetTransaction, method: getListByDate(with transaction_type), exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Transaction> getListByDate(String token, LocalDate date, PaymentMethod paymentMethod){
        try{
            String dateStr = HelpFuncs.localDateToString(date, "yyyy-MM-dd");
            String encodedDate = URLEncoder.encode(dateStr, StandardCharsets.UTF_8);
            getConnection("http://localhost:8000/api/admin/transaction/method?date=" + encodedDate + "&payment_method=" + paymentMethod.toString());
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
            System.out.println("class: GetTransaction, method: getListByDate(with payment_method), exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
