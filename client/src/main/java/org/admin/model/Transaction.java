package org.admin.model;

import lombok.Getter;
import lombok.Setter;
import org.admin.utils.HelpFuncs;
import org.admin.utils.TransactionType;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction extends Response {
    private Long id;
    private TransactionType type;
    private Long amount;
    private Integer count;
    private LocalDateTime timestamp;
    private Long unitId;

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("type", type.toString());
        obj.put("amount", amount);
        obj.put("count", count);
        obj.put("timestamp", HelpFuncs.localDateTimeToString(this.timestamp, "yyyy-MM-dd HH:mm:ss"));
        obj.put("unit_id", this.unitId);

        return obj;
    }

    public static Transaction fromJson(JSONObject obj){
        Transaction transaction = new Transaction();

        Long id = (Long) obj.get("id");
        TransactionType type = TransactionType.valueOf((String) obj.get("type"));
        Long amount = (Long) obj.get("amount");
        Integer count = (Integer) obj.get("count");
        LocalDateTime timestamp = LocalDateTime.parse((String) obj.get("timestamp"));
        Long unitId = (Long) obj.get("unit_id");

        transaction.setId(id);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCount(count);
        transaction.setTimestamp(timestamp);
        transaction.setUnitId(unitId);

        return transaction;
    }
}
