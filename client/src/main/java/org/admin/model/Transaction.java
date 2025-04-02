package org.admin.model;

import lombok.*;
import org.admin.utils.HelpFuncs;
import org.admin.utils.PaymentMethod;
import org.admin.utils.TransactionType;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends Response {
    private Long id;
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private Long purchaseAmount;
    private Integer count;
    private LocalDateTime timestamp;
    private Long adminId;
    private Long clientId;
    private Long unitId;

    public Transaction(int code, String message) {
        this.setCode(code);
        this.setMsg(message);
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("transaction_type", this.transactionType.toString());
        obj.put("payment_method", this.paymentMethod.toString());
        obj.put("purchase_amount", this.purchaseAmount);
        obj.put("count", this.count);
        obj.put("unit_id", this.unitId);
        obj.put("admin_id", this.adminId);
        obj.put("client_id", this.clientId);

        return obj;
    }

    public static Transaction fromJson(JSONObject obj){
        Transaction transaction = new Transaction();

        Long id = (Long) obj.get("id");
        TransactionType transactionType = TransactionType.valueOf((String) obj.get("transaction_type"));
        PaymentMethod paymentMethod = PaymentMethod.valueOf((String) obj.get("payment_method"));
        Long amount = (Long) obj.get("purchase_amount");
        Integer count = (Integer) obj.get("count");
        LocalDateTime timestamp = LocalDateTime.parse((String) obj.get("timestamp"));
        Long unitId = (Long) obj.get("unit_id");
        Long adminId = (Long) obj.get("admin_id");
        Long clientId = (Long) obj.get("client_id");

        transaction.setId(id);
        transaction.setTransactionType(transactionType);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setPurchaseAmount(amount);
        transaction.setCount(count);
        transaction.setTimestamp(timestamp);
        transaction.setUnitId(unitId);
        transaction.setAdminId(adminId);
        transaction.setClientId(clientId);

        transaction.setCode(200);
        return transaction;
    }
}
