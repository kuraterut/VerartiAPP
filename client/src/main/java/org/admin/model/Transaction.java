package org.admin.model;

import org.admin.utils.HelpFuncs;
import org.admin.utils.TransactionType;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public class Transaction extends Response {
    private Long id;
    private TransactionType type;
    private Long amount;
    private Integer count;
    private LocalDateTime timestamp;
    private Long unitId;

    public Long getId()                 {return this.id;}
    public TransactionType getType()    {return this.type;}
    public Long getAmount()             {return this.amount;}
    public Integer getCount()           {return this.count;}
    public LocalDateTime getTimestamp() {return this.timestamp;}
    public Long getUnitId()             {return this.unitId;}


    public void setId(Long id)                          { this.id = id;}
    public void setType(TransactionType type)           { this.type = type;}
    public void setAmount(Long amount)                  { this.amount = amount;}
    public void setCount(Integer count)                 { this.count = count;}
    public void setTimestamp(LocalDateTime timestamp)   { this.timestamp = timestamp;}
    public void setUnitId(Long unitId)                  { this.unitId = unitId;}

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
